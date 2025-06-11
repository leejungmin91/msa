package com.store.product.service;


import com.store.member.entity.MemberEntity;
import com.store.member.entity.ShippingEntity;
import com.store.product.domain.ProductCreateDomain;
import com.store.product.domain.ProductUpdateDomain;
import com.store.product.entity.ProductEntity;
import com.store.product.entity.ProductImageEntity;
import com.store.product.repository.ProductImageRepository;
import com.store.product.repository.ProductRepository;
import com.store.common.dto.FileInfoResponse;
import com.store.common.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final FileStorageService fileStorageService;

    public List<ProductEntity> getProductsWithImages() {
        return productRepository.findAllWithImages();
    }

    public Page<ProductEntity> getProductsToPaging(int page, int size, String displayYn) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findAllByUseYnAndDisplayYn(pageable, displayYn);
    }

    public ProductEntity getById(Long id, String displayYn) {
        return productRepository.findByIdWithOptionalDisplayYn(id, displayYn)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<ProductEntity> getByIdIn(List<Long> ids, String displayYn){
        return productRepository.findAllByIdInWithOptionalDisplayYn(ids, displayYn);
    }

    public ProductEntity getByIdContaining(Long id) {
        return productRepository.findByIdWithImages(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public ProductEntity getByNameContaining(String name) {
        return productRepository.findByNameContainingWithImages(name)
                .orElseThrow(EntityNotFoundException::new);
    }

    /**
     * 파일 등록
     * @param productCreateDomain
     * @return
     */
    @Transactional
    public ProductEntity register(ProductCreateDomain productCreateDomain) {
        duplicateProductCheck(productCreateDomain.name());

        FileInfoResponse mainFileInfo = fileStorageService.storeFile(productCreateDomain.mainImage());
        FileInfoResponse descriptionFileInfo = fileStorageService.storeFile(productCreateDomain.descriptionImage());

        ProductEntity productEntity = ProductEntity.create(productCreateDomain, mainFileInfo, descriptionFileInfo);
        ProductEntity savedProductEntity = productRepository.save(productEntity);

        List<ProductImageEntity> imageEntities = saveProductImages(savedProductEntity, productCreateDomain.subImages());
        savedProductEntity.addImages(imageEntities);

        return savedProductEntity;
    }

    /**
     * 파일 이름 중복체크
     * @param name
     */
    private void duplicateProductCheck(String name) {
        boolean isProduct = productRepository.existsByName(name);
        if (isProduct) throw new IllegalStateException("이미 등록된 상품입니다.");
    }

    /**
     * 파일 수정
     * @param id
     * @param productUpdateDomain
     * @return
     */
    @Transactional
    public ProductEntity update(Long id, ProductUpdateDomain productUpdateDomain) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        FileInfoResponse mainFileInfo = fileStorageService.storeFile(productUpdateDomain.mainImage());
        FileInfoResponse descriptionFileInfo = fileStorageService.storeFile(productUpdateDomain.descriptionImage());

        productEntity.update(productUpdateDomain, mainFileInfo, descriptionFileInfo);

        List<ProductImageEntity> imageEntities = saveProductImages(productEntity, productUpdateDomain.subImages());
        productEntity.addImages(imageEntities);

        return productEntity;
    }

    /**
     * 파일 이미지 저장
     * @param product
     * @param imageFiles
     * @return
     */
    private List<ProductImageEntity> saveProductImages(ProductEntity product, List<MultipartFile> imageFiles) {
        List<ProductImageEntity> imageEntities = new ArrayList<>();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            int displayOrder = 0;
            for (MultipartFile file : imageFiles) {
                FileInfoResponse imageFileInfo = fileStorageService.storeFile(file);
                ProductImageEntity imageEntity = ProductImageEntity.create(product, imageFileInfo, displayOrder);
                productImageRepository.save(imageEntity);
                imageEntities.add(imageEntity);
                displayOrder++;
            }
        }
        return imageEntities;
    }

    @Transactional
    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        product.deleteProduct();
    }

}
