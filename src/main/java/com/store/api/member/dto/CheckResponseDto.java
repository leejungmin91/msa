package com.store.api.member.dto;

import com.store.api.member.entity.MemberEntity;
import com.store.common.config.security.Role;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CheckResponseDto {
    private int cartCount;
    private String role;

    public static CheckResponseDto create(int cartCount, String role) {
        return CheckResponseDto.builder()
                .cartCount(cartCount)
                .role(role)
                .build();
    }
}
