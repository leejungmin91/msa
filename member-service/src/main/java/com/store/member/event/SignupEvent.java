package com.store.member.event;

import lombok.Builder;

@Builder
public record SignupEvent (String email, String name) {
}
