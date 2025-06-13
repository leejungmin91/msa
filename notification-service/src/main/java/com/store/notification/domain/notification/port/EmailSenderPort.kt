package com.store.notification.domain.notification.port

interface EmailSenderPort {
    fun sendEmail(email: String, subject: String, body: String)
}
