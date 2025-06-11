package com.store.member.domain;

import lombok.Builder;

@Builder
public record MemberUpdateDomain(String password, String phone) {
}
