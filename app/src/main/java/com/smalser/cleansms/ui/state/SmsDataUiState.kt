package com.smalser.cleansms.ui.state

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

data class SmsDataUiState(val message: String, val timestamp: LocalDateTime) {
    fun formatTimestamp(): String {
        val today = LocalDate.now()

        return if (today == timestamp.toLocalDate()) {
            "${timestamp.hour}:${if(timestamp.minute < 10) "0${timestamp.minute}" else timestamp.minute}"
        } else {
            "${timestamp.dayOfMonth} ${
                timestamp.month.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                )
            }"
        }
    }
}