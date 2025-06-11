package com.store.api.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.api.product.entity.ProductEntity;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CartItemResponseDto {
    private Long productId;
    private String name;
    private Long price;
    private String mainFileName;
    private String mainImageUrl;
    private int quantity;

    private int discountRate;

    public CartItemResponseDto increaseQuantity(int add) {
        return new CartItemResponseDto(this.productId, this.name, this.price, this.mainFileName, this.mainImageUrl, this.quantity + add, this.discountRate);
    }

    public CartItemResponseDto changeQuantity(int quantity) {
        return new CartItemResponseDto(this.productId, this.name, this.price, this.mainFileName, this.mainImageUrl, quantity, this.discountRate);
    }

    public static CartItemResponseDto of(CartItemResponseDto cached, ProductEntity product) {
        Long discount = (long) product.getDiscountRate();
        Long price = product.getPrice();
        if(discount > 0){
            price = price - (long) Math.round(product.getPrice() / product.getDiscountRate());
        }
        return CartItemResponseDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(price)
                .mainFileName(product.getMainFileName())
                .mainImageUrl(product.getMainImageUrl())
                .quantity(cached.getQuantity())
                .discountRate(product.getDiscountRate())
                .build();
    }

}
