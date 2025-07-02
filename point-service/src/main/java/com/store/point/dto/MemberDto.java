package com.store.point.dto;

import com.store.common.dto.commonMemberDto;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class MemberDto extends commonMemberDto {
    private String phone;
}
