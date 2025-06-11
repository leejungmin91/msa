package com.store.api.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.api.member.entity.MemberEntity;
import com.store.api.order.dto.OrderResponseDto;
import com.store.api.order.entity.OrderEntity;
import com.store.common.config.security.Role;
import com.store.common.util.CryptoService;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jasypt.util.text.BasicTextEncryptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
