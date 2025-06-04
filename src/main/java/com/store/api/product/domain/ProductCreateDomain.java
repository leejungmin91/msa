package com.store.api.product.domain;

import com.store.api.product.entity.ProductImageEntity;
import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record ProductCreateDomain(@RequestParam String name, @RequestParam Long price, @RequestParam int discountRate, @RequestPart("mainImage") MultipartFile mainImage,
                                  @RequestPart("subImages") List<MultipartFile> subImages, @RequestPart("descriptionImage") MultipartFile descriptionImage) {
}
