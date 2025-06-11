package com.store.order.repository;

import com.store.order.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, OrderRepositoryCustom{
    List<OrderEntity> findByMemberId(Long memberId);

//    @EntityGraph(attributePaths = {"orderItems"})

    Optional<OrderEntity> findByOrderNo(String orderNo);

    Page<OrderEntity> findAll(Pageable pageable);
}
