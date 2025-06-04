package com.store.api.order.controller;


import com.store.common.annotation.MemberAuthorize;
import com.store.api.order.domain.CartDomain;
import com.store.api.order.dto.CartItemResponseDto;
import com.store.api.order.service.CartService;
import com.store.common.annotation.CurrentUser;
import com.store.common.config.security.CustomUserDetails;
import com.store.common.http.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@MemberAuthorize
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart")
public class CartRestController {

    private final CartService cartService;

    /**
     * 장바구니 조회
     * @param userDetails
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getCart(@CurrentUser CustomUserDetails userDetails) {
        List<CartItemResponseDto> cartItems = cartService.getCart(userDetails.getId());
        return ResponseEntity.ok()
                .body(ApiResponse.success(cartItems));
    }

    /**
     * 장바구니 추가
     * @param userDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse> addCart(@CurrentUser CustomUserDetails userDetails, @RequestBody CartDomain cartDomain) {
        List<CartItemResponseDto> cartItems = cartService.addToCart(userDetails.getId(), cartDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success(cartItems));
    }

    /**
     * 장바구니 수량변경
     * @param userDetails
     * @return
     */
    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse> changeQuantity(@CurrentUser CustomUserDetails userDetails, @PathVariable Long productId, @RequestBody CartDomain cartDomain) {
        List<CartItemResponseDto> cartItems = cartService.updateCartItem(userDetails.getId(), productId, cartDomain.quantity());
        return ResponseEntity.ok()
                .body(ApiResponse.success(cartItems));
    }

    /**
     * 장바구니 상품 삭제
     * @param userDetails
     * @return
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteCartByProductId(@CurrentUser CustomUserDetails userDetails, @PathVariable Long productId) {
        cartService.removeFromCart(userDetails.getId(), productId);
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }

    /**
     * 장바구니 전체 삭제
     * @param userDetails
     * @return
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCart(@CurrentUser CustomUserDetails userDetails) {
        cartService.clearCart(userDetails.getId());
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }

}
