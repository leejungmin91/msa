package com.store.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.member.entity.MemberEntity;
import com.store.member.util.CryptoService;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
public class MemberResponseDto {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String approvalYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date approvalDate;
    private String role;

    public static MemberResponseDto from(MemberEntity member, CryptoService cryptoService) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phone(cryptoService.safeDecrypt(member.getPhone()))
                .approvalYn(member.getApprovalYn())
                .approvalDate(member.getApprovalDate())
                .role(member.getRole().getLabel())
                .build();
    }
}
