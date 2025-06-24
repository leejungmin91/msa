package com.store.point.service;

import com.store.point.domain.PointHistoryDomain;
import com.store.point.domain.PointSaveDomain;
import com.store.point.entity.PointEntity;
import com.store.point.entity.PointHistoryEntity;
import com.store.point.repository.PointHistoryRepository;
import com.store.point.repository.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePointHistory(PointHistoryDomain pointHistoryDomain) {
        PointHistoryEntity pointHistoryEntity = new PointHistoryEntity(pointHistoryDomain.userId()
                , pointHistoryDomain.point()
                , pointHistoryDomain.description()
                , pointHistoryDomain.type());
        pointHistoryRepository.save(pointHistoryEntity);
    }
}
