package com.store.api.order.domain;

import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemDomain {
    private Long productId;
    private String productName;
    private int quantity;
    private Long orderPrice;
}
