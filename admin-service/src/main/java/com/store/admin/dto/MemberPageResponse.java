package com.store.api.admin.dto;

import com.store.api.member.dto.MemberResponseDto;
import com.store.common.dto.PageResponse;
import org.springframework.data.domain.Page;

public class MemberPageResponse extends PageResponse<MemberResponseDto> {
    public MemberPageResponse(Page<MemberResponseDto> page) {
        super(page);
    }
}
