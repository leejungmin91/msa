package com.store.notification.infrastructure.mail

import com.store.notification.domain.notification.port.EmailSenderPort
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class MailSenderAdapter(
    private val mailSender: JavaMailSender
) : EmailSenderPort {

    override fun sendEmail(email: String, subject: String, body: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.subject = subject
        message.text = body
        mailSender.send(message)
    }
}
