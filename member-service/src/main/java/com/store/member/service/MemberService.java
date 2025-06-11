package com.store.member.service;


import com.store.member.domain.MemberSignUpDomain;
import com.store.member.domain.MemberUpdateDomain;
import com.store.member.dto.MemberResponseDto;
import com.store.member.entity.MemberEntity;
import com.store.member.repository.MemberRepository;
import com.store.common.config.security.Role;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import lombok.RequiredArgsConstructor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BasicTextEncryptor basicTextEncryptor;

    public List<MemberEntity> getAll() {
        return memberRepository.findAll();
    }

    public Page<MemberEntity> getAllToPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return memberRepository.findAll(pageable);
    }

    public Page<MemberEntity> getMembersByRoleToPaging(int page, int size, Role role) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return memberRepository.findByRole(role, pageable);
    }

    public MemberEntity getById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MemberEntity getByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
    }

    /**
     * 회원 가입
     * @param memberSignUpDomain
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MemberEntity register(MemberSignUpDomain memberSignUpDomain) {
        MemberEntity member = MemberEntity.signUp(memberSignUpDomain, passwordEncoder.encode(memberSignUpDomain.password()), basicTextEncryptor.encrypt(memberSignUpDomain.phone()));
        duplicateMemberCheck(member.getEmail());
        return memberRepository.save(member);
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberEntity update(Long id, MemberUpdateDomain memberUpdateDomain) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        String encodedPassword = null;
        if (memberUpdateDomain.password() != null && !memberUpdateDomain.password().isBlank()) {
            encodedPassword = passwordEncoder.encode(memberUpdateDomain.password());
        }

        String encryptedPhone = basicTextEncryptor.encrypt(memberUpdateDomain.phone());

        // dirty checking
        return MemberEntity.update(
                member,
                encodedPassword,
                encryptedPhone
        );
    }

    /**
     * 중복회원 조회
     *
     * @param email
     */
    private void duplicateMemberCheck(String email) {
        boolean isMember = memberRepository.existsByEmail(email);
        if (isMember) throw new ApiException(ApiCode.DUPLICATE_MEMBER);
    }
}
