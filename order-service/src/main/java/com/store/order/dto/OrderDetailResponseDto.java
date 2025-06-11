package com.store.order.dto;

import com.store.order.entity.OrderDetailEntity;
import com.store.order.entity.OrderPayDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderDetailResponseDto {
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

    public static OrderDetailResponseDto from(OrderDetailEntity entity) {
        return OrderDetailResponseDto.builder()
                .orderName(entity.getOrderName())
                .orderPhone(entity.getOrderPhone())
                .orderAddress(entity.getOrderAddress())
                .orderAddressDetail(entity.getOrderAddressDetail())
                .orderZipCode(entity.getOrderZipCode())
                .orderDate(entity.getOrderDate())
                .invoiceName(entity.getInvoiceName())
                .invoiceNo(entity.getInvoiceNo())
                .build();
    }
}
