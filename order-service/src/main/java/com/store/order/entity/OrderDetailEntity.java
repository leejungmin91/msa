package com.store.order.entity;

import com.store.order.domain.OrderCreateDomain;
import com.store.order.domain.OrderItemDomain;
import com.store.order.domain.OrderPrepareDomain;
import com.store.order.dto.OrderDetailResponseDto;
import com.store.order.dto.OrderInvoiceRequestDto;
import com.store.product.entity.ProductEntity;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "ORDER_DETAIL")
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity orders;

    private String orderName;

    private String orderPhone;

    private String orderAddress;

    private String orderAddressDetail;

    private String orderZipCode;

    private String invoiceName;

    private String invoiceNo;

    @CreatedDate
    private LocalDateTime orderDate; // 주문일자

    // 생성 메서드
    public static OrderDetailEntity create(OrderPrepareDomain orderPrepareDomain, OrderEntity orders) {
        OrderDetailEntity orderDetail = new OrderDetailEntity();
        orderDetail.orderName = orderPrepareDomain.ordNm();
        orderDetail.orderPhone = orderPrepareDomain.ordTel();
        orderDetail.orderAddress = orderPrepareDomain.orderAddress();
        orderDetail.orderAddressDetail = orderPrepareDomain.orderAddressDetail();
        orderDetail.orderZipCode = orderPrepareDomain.orderZipcode();
        orderDetail.orders = orders;

        return orderDetail;
    }

    public void setInvoice(OrderEntity order, OrderInvoiceRequestDto dto) {
        OrderDetailEntity orderDetailEntity =  order.getOrderDetail();
        orderDetailEntity.invoiceNo = dto.getInvoiceNo();
        orderDetailEntity.invoiceName = dto.getInvoiceName();
//        return orderDetailEntity;
    }
}
