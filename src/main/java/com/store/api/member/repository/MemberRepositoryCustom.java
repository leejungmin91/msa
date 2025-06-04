package com.store.api.member.repository;

import com.store.api.member.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepositoryCustom {

    Optional<MemberEntity> findOrderByEmail(String email);
    
}
