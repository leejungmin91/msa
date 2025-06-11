package com.store.member.repository;

import com.store.member.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {
    Optional<ShippingEntity> findByMemberIdAndMainYn(Long memberId, String mainYn);
    Optional<ShippingEntity> findByIdAndMemberId(Long id, Long memberId);
    List<ShippingEntity> findByMemberIdOrderByMainYnDescIdAsc(Long memberId);
}
