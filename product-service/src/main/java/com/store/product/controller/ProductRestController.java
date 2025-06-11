package com.store.product.controller;


import com.store.product.entity.ProductEntity;
import com.store.common.http.ApiResponse;
import com.store.product.domain.ProductCreateDomain;
import com.store.product.domain.ProductUpdateDomain;
import com.store.product.dto.ProductResponseDto;
import com.store.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${prefix}/product")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getProducts() {
        List<ProductEntity> products = productService.getProductsWithImages();

        return ResponseEntity.ok()
                .body(ApiResponse.success(products.stream()
                        .map(ProductResponseDto::from)
                        .toList()));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ProductEntity product = productService.getById(id, "Y");
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        ProductResponseDto.from(product)
                ));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getProductByNameContaining(@PathVariable String name) {
        ProductEntity product = productService.getByNameContaining(name);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        ProductResponseDto.from(product)
                ));
    }

}
