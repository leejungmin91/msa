package com.store.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.common.dto.FileInfoResponse;
import lombok.Getter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

@Getter
@Table(name = "PRODUCT_IMAGE")
@Entity
@Accessors(chain = true)
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    private String fileName;

    private String imageUrl;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private int displayOrder;

    public static ProductImageEntity create(ProductEntity product, FileInfoResponse imagesFileInfo, int displayOrder) {
        ProductImageEntity image = new ProductImageEntity();
        image.product = product;
        image.displayOrder = displayOrder;
        image.fileName = imagesFileInfo.fileName();
        image.imageUrl = imagesFileInfo.fileUrl();
        return image;
    }
}
