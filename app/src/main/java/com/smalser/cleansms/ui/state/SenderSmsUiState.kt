package com.smalser.cleansms.ui.state

import android.util.Log
import java.time.LocalDateTime

data class SenderSmsUiState(val sender: SenderUiState, val sms: List<SmsDataUiState>)

val smsGenerator: (Int) -> SenderSmsUiState = { i ->
    val sender = SenderUiState("Singtel $i", null)
    val sms = listOf(
        SmsDataUiState(
            "<ADV> Some super cool proposal specially for you!",
            LocalDateTime.now().minusDays(i.toLong())
        )
    )
    val senderWithSms = SenderSmsUiState(sender, sms)
    Log.i("SmsGenerator", senderWithSms.toString())
    senderWithSms
}