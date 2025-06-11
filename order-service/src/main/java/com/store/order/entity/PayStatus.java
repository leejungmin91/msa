package com.store.api.order.entity;

import lombok.Getter;

@Getter
public enum PayStatus {
    PENDING, PAID, CANCELED, FAILED
}
