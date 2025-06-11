package com.store.api.order.dto;

import com.store.api.order.domain.OrderItemDomain;
import com.store.api.order.entity.OrderEntity;
import com.store.api.order.entity.OrderItemEntity;
import com.store.api.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderItemResponseDto {
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


    public static OrderItemResponseDto from(OrderItemEntity orderItem) {
        OrderEntity order = orderItem.getOrders();

        return OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .productPrice(orderItem.getOrderPrice())
                .productQuantity(orderItem.getQuantity())
                .productMainFileName(orderItem.getProduct() != null ? orderItem.getProduct().getMainFileName() : null)
                .productMainImageUrl(orderItem.getProduct() != null ? orderItem.getProduct().getMainImageUrl() : null)
                .orderNo(order != null ? order.getOrderNo() : null)
                .orderDate(order != null ? order.getOrderDate() : null)
                .status(order != null ? order.getStatus().name() : null)
                .statusText(order != null ? order.getStatus().getDisplayName() : null)
                .build();
    }
}
