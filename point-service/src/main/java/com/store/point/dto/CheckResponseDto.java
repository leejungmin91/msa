package com.store.member.dto;

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
