package com.store.point.consumer;

import com.store.common.event.SignupEvent;
import com.store.point.domain.PointSaveDomain;
import com.store.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignupEventConsumer {

    private final PointService pointService;

    @KafkaListener(topics = "user.signup.point", groupId = "point-group")
    public void consume(SignupEvent signupEvent) {
        // 회원가입 성공!
        log.info("signup success: {}", signupEvent.toString());
        PointSaveDomain pointSaveDomain = new PointSaveDomain(signupEvent.id(), 1000L, "회원가입 시 지급되는 포인트", "signup");
        pointService.savePoint(pointSaveDomain);
    }
}
