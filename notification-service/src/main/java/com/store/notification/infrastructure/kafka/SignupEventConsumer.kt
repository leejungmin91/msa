package com.store.notification.infrastructure.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.store.notification.domain.notification.model.SignupEvent
import com.store.notification.domain.notification.service.NotificationService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SignupEventConsumer(
    private val notificationService: NotificationService
) {
    private val objectMapper = jacksonObjectMapper()

    @KafkaListener(topics = ["user.signup"], groupId = "notification-group")
    fun consume(record: ConsumerRecord<String, String>) {
        val message = record.value()
        try {
            val event: SignupEvent = objectMapper.readValue(message)
            notificationService.sendSignupEmail(event)
            println("Email sent to ${event.email}")
        } catch (e: Exception) {
            println("Failed to process message: ${e.message}")
        }
    }
}
