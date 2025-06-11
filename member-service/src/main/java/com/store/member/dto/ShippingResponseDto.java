package com.store.member.dto;

import com.store.member.entity.MemberEntity;
import com.store.member.entity.ShippingEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ShippingResponseDto {
    private Long id;
    private String subject;
    private String name;
    private String phone;
    private String address;
    private String addressDetail;
    private String zipcode;
    private String mainYn;

    public static ShippingResponseDto from(ShippingEntity shipping) {
        if(shipping == null) return null;
        return ShippingResponseDto.builder()
                .id(shipping.getId())
                .subject(shipping.getSubject())
                .name(shipping.getName())
                .phone(shipping.getPhone())
                .address(shipping.getAddress())
                .addressDetail(shipping.getAddressDetail())
                .zipcode(shipping.getZipcode())
                .mainYn(shipping.getMainYn())
                .build();
    }
}
