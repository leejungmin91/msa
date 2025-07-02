package com.store.member.controller;


import com.store.member.annotation.CurrentUser;
import com.store.member.config.security.CustomUserDetails;
import com.store.member.domain.MemberSignUpDomain;
import com.store.member.domain.MemberUpdateDomain;
import com.store.member.dto.MemberResponseDto;
import com.store.member.entity.MemberEntity;
import com.store.member.service.MemberService;
import com.store.common.http.ApiResponse;
import com.store.member.util.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/member")
public class MemberRestController {

    private final MemberService memberService;
    private final CryptoService cryptoService;

    @GetMapping("/@me")
    public ResponseEntity<ApiResponse> getMyInfo(@CurrentUser CustomUserDetails userDetails) {
        MemberEntity member = memberService.getByEmail(userDetails.getEmail());
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        MemberResponseDto.from(member, cryptoService))
                );
    }

    @PatchMapping("/@me")
    public ResponseEntity<ApiResponse> updateMember(@CurrentUser CustomUserDetails userDetails, @RequestBody MemberUpdateDomain memberUpdateDomain) {
        MemberEntity member = memberService.update(userDetails.getId(), memberUpdateDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        MemberResponseDto.from(member, cryptoService))
                );
    }

    /**
     * 회원가입
     *
     * @param memberSignUpDomain
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody MemberSignUpDomain memberSignUpDomain) {
        MemberEntity member = memberService.register(memberSignUpDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        MemberResponseDto.from(member, cryptoService))
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserInfo(@PathVariable Long id) {
        MemberEntity member = memberService.getById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        MemberResponseDto.from(member, cryptoService))
                );
    }


}
