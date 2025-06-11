package com.store.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberOrderItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long productPrice;
    private int productQuantity;

    // Order table
    private String orderNo;
    private LocalDateTime orderDate;
    private String status;
    private String statusText;

    // Product table
    private String productMainFileName;
    private String productMainImageUrl;
}
