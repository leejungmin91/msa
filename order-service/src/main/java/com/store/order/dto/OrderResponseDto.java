package com.store.api.order.dto;

import com.store.api.order.domain.OrderItemDomain;
import com.store.api.order.entity.OrderEntity;
import com.store.api.order.entity.OrderItemEntity;
import com.store.api.order.entity.OrderPayDetailEntity;
import com.store.api.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponseDto {
    private Long id;
    private Long memberId;
    private String orderNo;
    private List<OrderItemResponseDto> orderItems;
    private LocalDateTime orderDate;
    private String status;
    private String statusText;
    private OrderPayResponseDto orderPayResponseDto;
    private OrderDetailResponseDto orderDetailResponseDto;


    public static OrderResponseDto from(OrderEntity order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .orderNo(order.getOrderNo())
                .orderItems(order.getOrderItems().stream()
                        .map(OrderItemResponseDto::from)
                        .toList())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .statusText(order.getStatus().getDisplayName())
                .orderPayResponseDto(OrderPayResponseDto.from(order.getOrderPayDetail()))
                .orderDetailResponseDto(OrderDetailResponseDto.from(order.getOrderDetail()))
                .build();
    }
}
