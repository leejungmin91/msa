package com.store.api.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.api.member.entity.MemberEntity;
import com.store.api.member.repository.MemberRepositoryCustom;
import com.store.api.order.entity.OrderItemEntity;
import com.store.api.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.store.api.order.entity.QOrderDetailEntity.orderDetailEntity;
import static com.store.api.order.entity.QOrderEntity.orderEntity;
import static com.store.api.order.entity.QOrderItemEntity.orderItemEntity;
import static com.store.api.order.entity.QOrderPayDetailEntity.orderPayDetailEntity;
import static com.store.api.product.entity.QProductEntity.productEntity;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<OrderItemEntity> findOrderItemByConditions(Long memberId, String period, String status) {
        return query.selectFrom(orderItemEntity)
                .join(orderItemEntity.orders, orderEntity).fetchJoin()
                .join(orderEntity.orderDetail, orderDetailEntity).fetchJoin()
                .join(orderEntity.orderPayDetail, orderPayDetailEntity).fetchJoin()
                .join(orderItemEntity.product, productEntity).fetchJoin()
                .where(
                        memberIdEq(memberId),
                        orderDateAfter(period),
                        orderStatusEq(status)
                )
                .fetch();
    }

    public Map<OrderStatus, Long> countOrderItemsByStatusMap(Long memberId, String period) {
        return query.select(orderEntity.status, orderItemEntity.count())
                .from(orderItemEntity)
                .join(orderItemEntity.orders, orderEntity)
                .where(
                        memberIdEq(memberId),
                        orderDateAfter(period)
                )
                .groupBy(orderEntity.status)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> Optional.ofNullable(tuple.get(orderEntity.status)).orElse(OrderStatus.COMPLETE),
                        tuple -> Optional.ofNullable(tuple.get(orderItemEntity.count())).orElse(0L)
                ));
    }

    @Override
    public List<OrderItemEntity> findOrderComplete(Long memberId, String ordNo) {
        return query.selectFrom(orderItemEntity)
                .join(orderItemEntity.orders, orderEntity).fetchJoin()
                .join(orderItemEntity.product, productEntity).fetchJoin()
                .where(
                        orderEntity.orderNo.eq(ordNo)
                )
                .fetch();
    }

    private BooleanExpression ordNo(String ordNo) {
        return ordNo != null ? orderEntity.orderNo.eq(ordNo) : null;
    }
    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? orderEntity.memberId.eq(memberId) : null;
    }

    private BooleanExpression orderDateAfter(String period) {
        LocalDateTime from;

        if (period == null || period.equals("3m")) {
            from = LocalDateTime.now().minusMonths(3);
        } else if (period.equals("6m")) {
            from = LocalDateTime.now().minusMonths(6);
        } else if (period.equals("1y")) {
            from = LocalDateTime.now().minusYears(1);
        } else {
            // 예상치 못한 값이 들어올 경우에도 3개월 기본 적용
            from = LocalDateTime.now().minusMonths(3);
        }

        return orderEntity.orderDate.goe(from);
    }


    private BooleanExpression orderStatusEq(String status) {
        if (status == null || status.isBlank() || status.equalsIgnoreCase("ALL")) {
            return null;
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderEntity.status.eq(orderStatus);
        } catch (IllegalArgumentException e) {
            return null; // 잘못된 값이 들어오면 무시
        }
    }


}
