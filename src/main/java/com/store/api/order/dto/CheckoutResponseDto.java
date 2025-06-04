package com.store.api.order.dto;

import com.store.api.member.dto.MemberResponseDto;
import com.store.api.member.dto.ShippingResponseDto;
import com.store.api.product.dto.ProductResponseDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CheckoutResponseDto {
    private MemberResponseDto member;
    private ShippingResponseDto shipping;
    private List<CheckoutProductResponseDto> products;
}


