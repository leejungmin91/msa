package com.store.api.order.dto;

import com.store.api.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class OrderOverviewResponseDto {
    private List<OrderItemGroupedByDateDto> orders;
    private Map<OrderStatus, Long> statusCount;
}


