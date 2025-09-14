package com.smalser.cleansms

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.smalser.cleansms.model.SmsMessageData
import com.smalser.cleansms.ui.MainView
import com.smalser.cleansms.ui.MainViewModel
import com.smalser.cleansms.ui.state.SenderSmsUiState
import com.smalser.cleansms.ui.state.SenderUiState
import com.smalser.cleansms.ui.theme.CleanSMSTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    private val smsPermission = Manifest.permission.READ_SMS
    private val requestCode = 101

//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permission is granted. Continue the action or workflow in your
//                // app.
//            } else {
//                // Explain to the user that the feature is unavailable because the
//                // feature requires a permission that the user has denied. At the
//                // same time, respect the user's decision. Don't link to system
//                // settings in an effort to convince the user to change their
//                // decision.
//            }
//        }

    fun checkAndRequestSmsPermission(): List<SenderSmsUiState> {
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.REQUESTED_PERMISSION
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                // You can use the API that requires the permission.
//            }
//
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(
//                    Manifest.permission.REQUESTED_PERMISSION
//                )
//            }
//        }

        if (ContextCompat.checkSelfPermission(
                this,
                smsPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(smsPermission), requestCode)
            return listOf()
        } else {
            // Permission already granted — proceed to read SMS
            val smsMessages = readSmsMessages(this)

            //todo temp conversion
            val senderToData = mutableMapOf<String, SenderSmsUiState>()
            for (smsMessage in smsMessages) {
                val senderWithSms = senderToData.getOrDefault(
                    smsMessage.sender, SenderSmsUiState(
                        SenderUiState(
                            smsMessage.sender,
                            null
                        ), listOf()
                    )
                )

//                senderWithSms.sms.add(SmsDataUiState(smsMessage.message, smsMessage.receivedAt))
            }

            val senderWithSms = senderToData.values.toList()
            Log.i("MainActivity", senderWithSms.toString())
            return senderWithSms
        }
    }

    fun readSmsMessages(context: Context): List<SmsMessageData> {
        val smsList = mutableListOf<SmsMessageData>()
        val uri = "content://sms/inbox".toUri()
        val projection = arrayOf(
            Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.BODY,
            Telephony.TextBasedSmsColumns.DATE
        )
        val sortOrder = "date DESC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.use {
            val addressIdx = it.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.ADDRESS)
            val bodyIdx = it.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY)
            val dateIdx = it.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.DATE)

            while (it.moveToNext()) {
                val address = it.getString(addressIdx)
                val body = it.getString(bodyIdx)
                val timestamp = it.getLong(dateIdx)
                smsList.add(
                    SmsMessageData(
                        address,
                        body,
                        LocalDateTime.ofEpochSecond(timestamp, 0, java.time.ZoneOffset.UTC)
                    )
                )
            }
        }

        return smsList
    }

    fun promptSetDefaultSmsApp() {
        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
        startActivity(intent)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        promptSetDefaultSmsApp()
//        checkAndRequestSmsPermission()
//        val smsList = List(10) { smsGenerator(it) }
//        val smsList = checkAndRequestSmsPermission()

        enableEdgeToEdge()

        setContent {
            CleanSMSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(
                        modifier = Modifier.padding(innerPadding),
                        viewModel.uiState.collectAsState().value
                    )
                }
            }
        }
    }
}