package com.store.order.dto;

import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderItemEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderItemGroupedByDateDto {
    private String date;
    private List<OrderItemResponseDto> items;
}

