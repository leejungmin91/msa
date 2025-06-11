/*
package com.store.member.repository;

import com.store.member.entity.MemberEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.store.member.entity.QMemberEntity.memberEntity;
import static com.store.order.entity.QOrderEntity.orderEntity;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Optional<MemberEntity> findOrderByEmail(String email) {
        return Optional.ofNullable(query.selectFrom(memberEntity)
                .leftJoin(orderEntity).on(orderEntity.memberId.eq(memberEntity.id)).fetchJoin()
                .where(emailEq(email))
                .fetchFirst());
    }

    private BooleanExpression emailEq(String email) {
        return email != null ? memberEntity.email.eq(email) : null;
    }
}
*/
