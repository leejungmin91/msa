package com.store.point.repository;

import com.store.point.entity.PointEntity;
import com.store.point.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
}
