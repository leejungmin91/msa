package com.store.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDER("주문완료"),
    CANCEL("주문취소"),
    DELIVERY("배송중"),
    COMPLETE("배송완료");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

}
