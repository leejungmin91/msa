package com.store.api.member.dto;

import com.store.api.member.entity.MemberEntity;
import com.store.api.order.dto.OrderResponseDto;
import com.store.api.order.entity.OrderEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
public class MemberOrderResponseDto extends MemberResponseDto {
    @Builder.Default
    private List<OrderResponseDto> orders = new ArrayList<>();

    public static MemberOrderResponseDto from(MemberEntity member, List<OrderEntity> orders) {
        return MemberOrderResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .orders(orders != null
                        ? orders.stream()
                            .map(OrderResponseDto::from)
                            .toList()
                        : new ArrayList<>())
                .build();
    }

}
