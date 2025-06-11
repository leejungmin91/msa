package com.store.member.domain;

import lombok.Builder;

@Builder
public record SignInDomain(String email, String password) {
}
