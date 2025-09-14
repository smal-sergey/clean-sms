package com.smalser.cleansms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Parse SMS from intent
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val newSms = messages.joinToString { it.messageBody }

        Log.i("SmsReceiver", "Sms received: $newSms")
    }

}
