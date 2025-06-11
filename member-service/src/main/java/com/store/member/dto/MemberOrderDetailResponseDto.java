package com.store.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MemberOrderDetailResponseDto {
    private Long id;
    private Long memberId;
    private String orderNo;
    private String orderName;
    private String orderPhone;
    private String orderAddress;
    private String orderAddressDetail;
    private String orderZipCode;
    private LocalDateTime orderDate; // 주문일자
    private String invoiceName;
    private String invoiceNo;
}
