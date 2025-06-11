package com.store.api.order.entity;

import lombok.Getter;

@Getter
public enum PayType {
    CARD("카드"),
    MOBILE("핸드폰"),
    ACCOUNT_TRANSFER("계좌이체");

    private final String displayName;

    PayType(String displayName) {
        this.displayName = displayName;
    }
}
