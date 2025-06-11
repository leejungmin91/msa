package com.store.product.dto;

import com.store.product.entity.ProductImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductImageResponseDto {
    private final String fileName;
    private final String imageUrl;

    public static ProductImageResponseDto from(ProductImageEntity productImageEntity) {
        return new ProductImageResponseDto(
                productImageEntity.getFileName(),
                productImageEntity.getImageUrl()
        );
    }
}
