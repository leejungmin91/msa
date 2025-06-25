package com.store.point.service;

import com.store.point.domain.PointHistoryDomain;
import com.store.point.domain.PointSaveDomain;
import com.store.point.dto.PointDto;
import com.store.point.entity.PointEntity;
import com.store.point.repository.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryService pointHistoryService;

    public PointDto getUserPoint(Long userId) {
        PointEntity pointEntity = pointRepository.findByUserId(userId)
                .orElseThrow(EntityNotFoundException::new);

        return PointDto.from(pointEntity);
    }

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
