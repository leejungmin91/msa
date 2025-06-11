package com.store.product.entity;

import com.store.product.domain.ProductCreateDomain;
import com.store.product.domain.ProductUpdateDomain;
import com.store.common.dto.FileInfoResponse;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Table(name = "PRODUCT")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private int discountRate;

    @Column
    private String descriptionFileName;

    @Column
    private String descriptionImageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImageEntity> images = new ArrayList<>();

    @Column
    private String mainFileName;

    @Column
    private String mainImageUrl;

    @Column
    private String displayYn;

    @Column
    private String useYn;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date modified;

    // 생성
    public static ProductEntity create(ProductCreateDomain productCreateDomain
            , FileInfoResponse mainFileInfo
            , FileInfoResponse descriptionFileInfo) {
        ProductEntity product = new ProductEntity();
        product.name = productCreateDomain.name();
        product.price = productCreateDomain.price();
        product.discountRate = productCreateDomain.discountRate();
        product.descriptionFileName = descriptionFileInfo.fileName();
        product.descriptionImageUrl = descriptionFileInfo.fileUrl();
        product.images = new ArrayList<>();
        product.mainFileName = mainFileInfo.fileName();
        product.mainImageUrl = mainFileInfo.fileUrl();
        product.displayYn = "Y";
        product.useYn = "Y";
        return product;
    }

    // 업데이트
    public void update(ProductUpdateDomain productUpdateDomain
            , FileInfoResponse mainFileInfo
            , FileInfoResponse descriptionFileInfo) {
        if (productUpdateDomain.price() <= 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        }

        if (productUpdateDomain.discountRate() < 0 || productUpdateDomain.discountRate() > 100) {
            throw new IllegalArgumentException("할인률은 0 ~ 100 사이여야합니다.");
        }

        this.name = productUpdateDomain.name();
        this.price = productUpdateDomain.price();
        this.discountRate = productUpdateDomain.discountRate();
        this.displayYn = productUpdateDomain.displayYn();

        if(mainFileInfo != null) {
            this.mainFileName = mainFileInfo.fileName();
            this.mainImageUrl = mainFileInfo.fileUrl();
        }
        if(descriptionFileInfo != null) {
            this.descriptionFileName = descriptionFileInfo.fileName();
            this.descriptionImageUrl = descriptionFileInfo.fileUrl();
        }
    }

    // 이미지 추가
    public void addImages(List<ProductImageEntity> images) {
        if(images != null) {
            this.images.clear();
            this.images.addAll(images);
        }
    }

    public void deleteProduct() {
        this.useYn = "N";
    }
}

