package com.store.order.dto;

import com.store.member.dto.MemberResponseDto;
import com.store.member.dto.ShippingResponseDto;
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


