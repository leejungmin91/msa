package com.store.member.repository;

import com.store.member.entity.MemberEntity;
import com.store.common.config.security.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {
    Optional<MemberEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<MemberEntity> findAll(Pageable pageable);
    Page<MemberEntity> findByRole(Role role, Pageable pageable);
}
