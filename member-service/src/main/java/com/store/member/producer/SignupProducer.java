package com.store.member.producer;

import com.store.common.event.SignupEvent;
import com.store.common.util.KafkaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignupProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(SignupEvent event) {
        KafkaUtils.sendWithErrorHandler(kafkaTemplate, "user.signup", event);
        log.info("Kafka 전송 완료: {}", event.email());
    }
}
