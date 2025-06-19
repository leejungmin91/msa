package com.store.member.domain;

import lombok.Builder;

@Builder
public record MemberSignUpDomain(String email, String name, String password, String phone, String zipcode,
                                 String address) {
}
