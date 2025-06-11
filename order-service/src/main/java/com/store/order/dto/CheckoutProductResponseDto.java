package com.store.order.dto;

import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderItemEntity;
import com.store.product.entity.ProductEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CheckoutProductResponseDto {
    private Long productId;
    private String productName;
    private Long productPrice;
    private int productQuantity;
    private String productMainFileName;
    private String productMainImageUrl;
    private int discountRate;

    public static CheckoutProductResponseDto from(ProductEntity product, int productQuantity) {
        Long discount = (long) product.getDiscountRate();
        Long price = product.getPrice();
        if(discount > 0){
            price = price - (long) Math.round(product.getPrice() / product.getDiscountRate());
        }
        return CheckoutProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(price)
                .productQuantity(productQuantity)
                .productMainFileName(product.getMainFileName())
                .productMainImageUrl(product.getMainImageUrl())
                .discountRate(product.getDiscountRate())
                .build();
    }

    public static List<CheckoutProductResponseDto> fromCart(List<CartItemResponseDto> cart) {
        return cart.stream()
                .map(cartItem -> CheckoutProductResponseDto.builder()
                        .productId(cartItem.getProductId())
                        .productName(cartItem.getName())
                        .productPrice(cartItem.getPrice())
                        .productQuantity(cartItem.getQuantity())
                        .productMainFileName(cartItem.getMainFileName())
                        .productMainImageUrl(cartItem.getMainImageUrl())
                        .discountRate(cartItem.getDiscountRate())
                        .build())
                .toList();
    }
}
