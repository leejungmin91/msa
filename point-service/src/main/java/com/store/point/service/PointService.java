package com.store.point.service;

import com.store.common.event.SignupEvent;
import com.store.point.domain.PointHistoryDomain;
import com.store.point.domain.PointSaveDomain;
import com.store.point.entity.PointEntity;
import com.store.point.entity.PointHistoryEntity;
import com.store.point.repository.PointHistoryRepository;
import com.store.point.repository.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryService pointHistoryService;

    @Transactional
    public void savePoint(PointSaveDomain pointSaveDomain) {

        // 비관적 락 적용
        PointEntity selectUserPoint = pointRepository.findWithLockByUserId(pointSaveDomain.userId())
                .orElseThrow(EntityNotFoundException::new);

        PointEntity.updatePoint(selectUserPoint, pointSaveDomain.point());

        PointHistoryDomain pointHistoryDomain = new PointHistoryDomain(pointSaveDomain.userId()
                , pointSaveDomain.point()
                , pointSaveDomain.description()
                , pointSaveDomain.type() );

        pointHistoryService.savePointHistory(pointHistoryDomain);
    }
}
