package com.store.point.service;

import com.store.common.event.SignupEvent;
import com.store.point.entity.PointEntity;
import com.store.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public void savePoint(SignupEvent event) {
        PointEntity pointEntity = PointEntity.signUp(event, 1000L);
        pointRepository.save(pointEntity);
    }
}
