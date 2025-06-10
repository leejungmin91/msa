package com.store.api.member.domain;

import lombok.Builder;

@Builder
public record MemberUpdateDomain(String password, String phone) {
}
