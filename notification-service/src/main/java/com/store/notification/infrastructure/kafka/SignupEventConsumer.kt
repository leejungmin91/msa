package com.store.notification.infrastructure.kafka

import com.store.common.event.SignupEvent
import com.store.notification.domain.notification.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SignupEventConsumer(
    private val notificationService: NotificationService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["user.signup.mail"], groupId = "notification-group")
    fun consume(event: SignupEvent) {
        try {
            log.info("kafka message received $event")
            //notificationService.sendSignupEmail(event)
        } catch (e: Exception) {
            log.error("Failed to process message: ${e.stackTraceToString()}")
        }
    }
}
