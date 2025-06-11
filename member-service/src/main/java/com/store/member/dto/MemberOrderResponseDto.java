package com.store.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MemberOrderResponseDto extends MemberResponseDto {
    private Long id;
    private Long memberId;
    private String orderNo;
    private List<MemberOrderItemResponseDto> orderItems;
    private LocalDateTime orderDate;
    private String status;
    private String statusText;
    private MemberOrderPayResponseDto orderPayResponseDto;
    private MemberOrderDetailResponseDto orderDetailResponseDto;
}
