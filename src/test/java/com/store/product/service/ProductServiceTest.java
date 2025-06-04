package com.store.product.service;

import com.store.api.product.domain.ProductCreateDomain;
import com.store.api.product.dto.ProductResponseDto;
import com.store.api.product.entity.ProductEntity;
import com.store.api.product.entity.ProductImageEntity;
import com.store.api.product.repository.ProductRepository;
import com.store.api.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Profile("local")
@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    /*@Test
    void 상품정보_불러오기_테스트() {


        ProductCreateDomain productCreateDomain = ProductCreateDomain.builder()
                .name("하늘색우산")
                .price(10000L)
                .build();

        ProductEntity productEntity = ProductEntity.create(productCreateDomain);

        List<ProductImageEntity> productImageEntities = new ArrayList<>();

        ProductImageEntity productImageEntity = ProductImageEntity.create(productEntity, "하늘색우산.png", "resources/images/하늘색우산.png", 0);

        productImageEntities.add(productImageEntity);

        productEntity.addImages(productImageEntities);

        when(productRepository.findByNameContainingWithImages(any(String.class))).thenReturn(Optional.of(productEntity));

        //when
        ProductResponseDto productResponseDto = productService.getByNameContaining("하늘색우산");

        //then
        assertThat(productResponseDto.getName()).isEqualTo(productEntity.getName());
        assertThat(productResponseDto.getImages().size()).isEqualTo(productEntity.getImages().size());

        verify(productRepository, times(1)).findByNameContainingWithImages(any(String.class));
    }*/

}
