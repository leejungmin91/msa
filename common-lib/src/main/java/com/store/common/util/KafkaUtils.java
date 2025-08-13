/*
package com.store.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
public class KafkaUtils {

    public static <K, V> CompletableFuture<SendResult<K, V>> sendWithErrorHandler(
            KafkaTemplate<K, V> kafkaTemplate, String topic, V value) {

        CompletableFuture<SendResult<K, V>> future = kafkaTemplate.send(topic, value);
        future.exceptionally(ex -> {
            // 공통 오류처리
            // 알림 + DB 적재
            return null;
        });
        return future;
    }
}


*/
