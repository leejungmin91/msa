package com.store.api.member.domain;

import lombok.Builder;

@Builder
public record ShippingUpdateDomain(String subject, String name, String phone, String address, String addressDetail,
                                   String zipcode, String mainYn) {
}
