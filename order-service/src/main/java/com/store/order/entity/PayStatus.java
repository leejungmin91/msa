package com.store.order.entity;

import lombok.Getter;

@Getter
public enum PayStatus {
    PENDING, PAID, CANCELED, FAILED
}
