package com.store.product.domain;

import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record ProductUpdateDomain(@RequestParam String name, @RequestParam Long price, @RequestParam String displayYn, @RequestParam int discountRate, @RequestPart("mainImage") MultipartFile mainImage,
                                  @RequestPart("subImages") List<MultipartFile> subImages, @RequestPart("descriptionImage") MultipartFile descriptionImage) {
}
