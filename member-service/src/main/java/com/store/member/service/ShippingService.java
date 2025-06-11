package com.store.member.service;


import com.store.member.domain.ShippingCreateDomain;
import com.store.member.domain.ShippingUpdateDomain;
import com.store.member.entity.MemberEntity;
import com.store.member.entity.ShippingEntity;
import com.store.member.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final MemberService memberService;

    public ShippingEntity getMainShipping(Long memberId) {
        return shippingRepository.findByMemberIdAndMainYn(memberId, "Y")
                .orElse(null);
    }

    public ShippingEntity getMyShipping(Long shippingId, Long memberId) {
        return shippingRepository.findByIdAndMemberId(shippingId, memberId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<ShippingEntity> getShippingList(Long memberId) {
        return shippingRepository.findByMemberIdOrderByMainYnDescIdAsc(memberId);
    }

    @Transactional
    public void saveShipping(Long memberId, ShippingCreateDomain shippingCreateDomain) {
        MemberEntity member = memberService.getById(memberId);
        ShippingEntity shipping = ShippingEntity.create(shippingCreateDomain);

        shipping.addMember(member);
        member.getShipping().add(shipping);

        if("Y".equals(shippingCreateDomain.mainYn())) {
            // 대표 배송지 설정
            Optional<ShippingEntity> optionalMemberShipping = shippingRepository.findByMemberIdAndMainYn(memberId, "Y");

            optionalMemberShipping.ifPresent(shippingEntity -> shippingEntity.changeMainYn("N"));
        }

        shippingRepository.save(shipping);

    }

    @Transactional
    public ShippingEntity updateShipping(Long id, ShippingUpdateDomain shippingUpdateDomain) {
        ShippingEntity shipping = shippingRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        // dirty checking
        return shipping.updateShipping(shippingUpdateDomain);
    }

    @Transactional
    public void deleteShipping(Long id) {
        ShippingEntity shipping = shippingRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        shippingRepository.delete(shipping);
    }
}
