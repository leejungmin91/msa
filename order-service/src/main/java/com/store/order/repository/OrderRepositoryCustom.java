package com.store.api.order.repository;

import com.store.api.order.entity.OrderItemEntity;
import com.store.api.order.entity.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepositoryCustom {

    List<OrderItemEntity> findOrderItemByConditions(Long memberId, String period, String status);
    Map<OrderStatus, Long> countOrderItemsByStatusMap(Long memberId, String period);

    List<OrderItemEntity> findOrderComplete(Long memberId, String ordNo);
}
