package com.store.admin.service;

import com.store.member.entity.MemberEntity;
import com.store.member.service.MemberService;
import com.store.order.entity.OrderEntity;
import com.store.order.service.OrderService;
import com.store.product.entity.ProductEntity;
import com.store.product.service.ProductService;
import com.store.common.util.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private static final String INIT_PASSWORD = "0000";

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private final OrderService orderService;

    public Page<MemberEntity> getMembers(int page, int size) {
        return memberService.getMembersByRoleToPaging(page, size, Role.ROLE_USER);
    }

    public Page<MemberEntity> getPendingMembers(int page, int size) {
        return memberService.getMembersByRoleToPaging(page, size, Role.ROLE_PENDING);
    }

    @Transactional
    public void approvalMember(Long memberId, String approvalYn) {
        MemberEntity member = memberService.getById(memberId);

        // dirty checking
        MemberEntity.approvalMember(member, approvalYn);
    }

    @Transactional(rollbackFor = Exception.class)
    public void initPassword(Long memberId){
        MemberEntity member = memberService.getById(memberId);

        MemberEntity.passwordUpdate(member, passwordEncoder.encode(INIT_PASSWORD));
    }

    public Page<ProductEntity> getProducts(int page, int size) {
        return productService.getProductsToPaging(page, size, "Y");
    }

    public Page<OrderEntity> getOrders(int page, int size) {
        return orderService.getOrdersByRoleToPaging(page, size, Role.ROLE_USER);
    }


}
