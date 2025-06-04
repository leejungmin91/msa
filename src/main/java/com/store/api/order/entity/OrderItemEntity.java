package com.store.api.order.entity;

import com.store.api.order.domain.OrderItemDomain;
import com.store.api.product.entity.ProductEntity;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column
    private String productName;

    @Column
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orders;

    @Column
    private Long orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductEntity product;

    public static OrderItemEntity from(OrderItemDomain domain, OrderEntity orders) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.productId = domain.getProductId();
        entity.productName = domain.getProductName();
        entity.quantity = domain.getQuantity();
        entity.orderPrice = domain.getOrderPrice();
        entity.setOrders(orders);
        return entity;
    }

    public void setOrders(OrderEntity orders) {
        this.orders = orders;
        if (!orders.getOrderItems().contains(this)) {
            orders.getOrderItems().add(this);
        }
    }
}
