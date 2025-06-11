package com.store.order.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "@class"
)
@Builder
public record OrderCreateDomain(Long memberId, List<OrderItemDomain> orderItems, String orderName, String orderPhone,
                                String orderAddress, String orderAddressDetail, String orderZipCode, String payType) {

    public OrderCreateDomain addMemberId(Long memberId) {
        return new OrderCreateDomain(memberId, orderItems, orderName, orderPhone, orderAddress, orderAddressDetail, orderZipCode, payType);
    }
}
