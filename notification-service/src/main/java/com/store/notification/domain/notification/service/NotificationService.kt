package com.store.notification.domain.notification.service

import com.store.common.event.SignupEvent
import com.store.notification.domain.notification.port.EmailSenderPort
import org.springframework.stereotype.Service

@Service
class NotificationService(private val emailSender: EmailSenderPort) {
    fun sendSignupEmail(event: SignupEvent) {
        val subject = "환영합니다."
        val body = "안녕하세요 ${event.name} 님"
        emailSender.sendEmail(event.email, subject, body)
    }
}
