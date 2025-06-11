package com.store.member.config.security;

public enum Role {

    ROLE_PENDING("승인대기"),
    ROLE_USER("일반회원"),
    ROLE_ADMIN("관리자");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
