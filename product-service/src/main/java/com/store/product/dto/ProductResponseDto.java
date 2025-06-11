package com.store.api.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.api.product.entity.ProductEntity;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ProductResponseDto {
    private Long id;
    private String name;
    private Long price;
    private List<ProductImageResponseDto> images;
    private String descriptionFileName;
    private String descriptionImageUrl;
    private int discountRate;
    private String displayYn;

    private String mainFileName;
    private String mainImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date modified;

    public static ProductResponseDto from(ProductEntity product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .images(product.getImages().stream()
                        .map(ProductImageResponseDto::from)
                        .toList())
                .descriptionFileName(product.getDescriptionFileName())
                .descriptionImageUrl(product.getDescriptionImageUrl())
                .discountRate(product.getDiscountRate())
                .displayYn(product.getDisplayYn())
                .mainFileName(product.getMainFileName())
                .mainImageUrl(product.getMainImageUrl())
                .created(product.getCreated())
                .modified(product.getModified())
                .build();
    }
}
