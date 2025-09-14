package com.smalser.cleansms.model

import java.time.LocalDateTime

data class SmsMessageData(
    val sender: String,
    val message: String,
    val receivedAt: LocalDateTime
)