package com.store.api.order.entity;

import com.store.api.member.entity.MemberEntity;
import com.store.api.order.domain.OrderPrepareDomain;
import com.store.api.order.domain.OrderResultDomain;
import com.store.api.order.dto.OrderCancelResponseDto;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "ORDERS")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    private String orderNo;

    private Long memberId;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    private LocalDateTime orderDate; // 주문일자

    @LastModifiedDate
    private Date modified;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private OrderDetailEntity orderDetail;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private OrderPayDetailEntity orderPayDetail;

    public void cancel(OrderEntity order, OrderCancelResponseDto orderCancelResponseDto) {
        this.status = OrderStatus.CANCEL;
        this.orderPayDetail.cancel(order,orderCancelResponseDto);
    }

    public void validatePayment(Long redisMemberId, Long redisAmount) {
        if (!this.memberId.equals(redisMemberId)) {
            throw new ApiException(ApiCode.PAYMENT_MEMBER_DENIED);
        }
        if (!(this.orderPayDetail.getAmt()).equals(redisAmount.toString())) {
            throw new ApiException(ApiCode.PAYMENT_PAID_AMOUNT_DIFFERENT);
        }
    }


    // 생성 메서드
    public static OrderEntity create(OrderPrepareDomain orderPrepareDomain, OrderResultDomain orderResultDomain) {
        OrderEntity order = new OrderEntity();
        order.memberId = orderPrepareDomain.memberId();
        order.orderNo = orderPrepareDomain.ordNo();
        order.orderItems = orderPrepareDomain.orderItems()
                .stream()
                .map(itemDomain -> OrderItemEntity.from(itemDomain, order))
                .toList();

        order.status = OrderStatus.ORDER;

        // 결제 상세 저장
        order.orderDetail = OrderDetailEntity.create(orderPrepareDomain, order);
        order.payType = PayType.valueOf(orderResultDomain.payMethod());

        // PG 결과 저장
        order.orderPayDetail = OrderPayDetailEntity.create(orderResultDomain, order);

        return order;
    }
}
