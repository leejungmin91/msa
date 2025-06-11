package com.store.api.order.service;

import com.store.api.order.domain.CartDomain;
import com.store.api.order.dto.CartItemResponseDto;
import com.store.api.product.entity.ProductEntity;
import com.store.api.product.service.ProductService;
import com.store.common.util.RedisService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisService redisService;
    private final ProductService productService;

    private static final long CART_TTL_SECONDS = 60 * 60 * 24 * 30; // 30일

    private String getCartKey(Long userId) {
        return "cart:user:" + userId;
    }

    /**
     * UserId 로 장바구니 조회 후 상품정보 매핑
     * @param userId
     * @return
     */
    public List<CartItemResponseDto> getCart(Long userId) {
        //Object stored = redisService.get(getCartKey(userId));
        Object stored = redisService.rangeListAll(getCartKey(userId));
        if (stored instanceof List<?>) {
            Map<Long, CartItemResponseDto> cartMap = ((List<?>) stored).stream()
                    .filter(CartItemResponseDto.class::isInstance)
                    .map(o -> (CartItemResponseDto) o)
                    .collect(Collectors.toMap(CartItemResponseDto::getProductId, Function.identity()));

            List<Long> productIds = new ArrayList<>(cartMap.keySet());

            List<ProductEntity> products = productService.getByIdIn(productIds, "Y");

            return products.stream()
                    .map(p -> CartItemResponseDto.of(cartMap.get(p.getId()), p))
                    .toList();
        }
        return new ArrayList<>();
    }

    public List<CartItemResponseDto> addToCart(Long userId, CartDomain cartCreateDomain) {
        String key = getCartKey(userId);
        // 1) 현재 장바구니 리스트 전부 읽기
        List<CartItemResponseDto> cart = getCart(userId);

        // 2) 같은 상품 있는지 찾기 (인덱스까지)
        int idx = IntStream.range(0, cart.size())
                .filter(i -> cart.get(i).getProductId().equals(cartCreateDomain.productId()))
                .findFirst()
                .orElse(-1);

        if (idx >= 0) {
            // 3a) 기존 아이템 수량 변경
            CartItemResponseDto existing = cart.get(idx);
            CartItemResponseDto updated = existing.increaseQuantity(cartCreateDomain.quantity());
            redisService.setList(key, idx, updated);
        } else {
            // 3b) 새 아이템이라면 리스트 뒤에 push
            CartItemResponseDto newItem = CartItemResponseDto.builder()
                    .productId(cartCreateDomain.productId())
                    .quantity(cartCreateDomain.quantity())
                    .build();
            redisService.pushList(key, newItem);
        }

        // 4) TTL 재설정 (옵션)
        redisService.expire(key, CART_TTL_SECONDS);

        // 5) 최종 상태 리턴
        return getCart(userId);
    }

    // 수량 변경 (PATCH)
    public List<CartItemResponseDto> updateCartItem(Long userId, Long productId, int newQuantity) {
        // 존재하면 수량을 newQuantity로 set
        String key = getCartKey(userId);

        // 1) 현재 장바구니 리스트 전부 읽기
        List<CartItemResponseDto> cart = getCart(userId);
        if (cart.isEmpty()) {
            return cart;
        }

        // 2) 수정할 아이템의 인덱스 찾기
        int idx = IntStream.range(0, cart.size())
                .filter(i -> cart.get(i).getProductId().equals(productId))
                .findFirst()
                .orElse(-1);

        if (idx >= 0) {
            // 3) changeQuantity 로 절대값 변경
            CartItemResponseDto updated = cart.get(idx)
                    .increaseQuantity(newQuantity);
            // 4) Redis 리스트에서 해당 인덱스만 set
            redisService.setList(key, idx, updated);
            // 5) TTL(유효기간) 재설정
            redisService.expire(key, CART_TTL_SECONDS);
        }

        // 6) 최종 상태를 다시 꺼내서 반환
        return getCart(userId);
    }

    public void removeFromCart(Long userId, Long productId) {
        String key = getCartKey(userId);

        List<Object> rawList = redisService.rangeListAll(key);

        // 2) CartItemResponseDto인 항목만 필터링·캐스팅
        List<CartItemResponseDto> cart = rawList.stream()
                .filter(CartItemResponseDto.class::isInstance)
                .map(CartItemResponseDto.class::cast)
                .toList();

        cart.stream()
                .filter(item -> Objects.equals(item.getProductId(), productId))
                .findFirst()
                .ifPresent(itemToRemove -> {
                    redisService.removeList(key, itemToRemove);
                    redisService.expire(key, CART_TTL_SECONDS);
                });
    }


    public void clearCart(Long userId) {
        redisService.delete(getCartKey(userId));
    }
}
