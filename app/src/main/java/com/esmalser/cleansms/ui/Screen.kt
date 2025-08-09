package com.esmalser.cleansms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun MainView(modifier: Modifier, smsList: List<SenderWithSms>) {
    Column(modifier = modifier) {
        smsList.forEach {
            SmsRowPreviewView(modifier, it)
        }
    }

}

@Composable
fun SmsRowPreviewView(modifier: Modifier, senderWithSms: SenderWithSms) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .height(48.dp)
    ) {
        SenderAvatarPlaceholder(modifier = modifier)
        Column(modifier = modifier.padding(start = 10.dp)) {
            Text(modifier = modifier.padding(top = 5.dp), text = senderWithSms.sender.name)
            Text(
                modifier = modifier
                    .padding(top = 5.dp)
                    .width(200.dp),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = senderWithSms.sms.first().message
            )
        }
        Text(
            modifier = modifier
                .padding(5.dp)
                .width(30.dp),
            fontSize = 10.sp,
            text = senderWithSms.sms.first().formatTimestamp()
        )
    }
}

@Composable
fun SenderAvatarPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(color = Color(0xFF1A73E8), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Sender avatar",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    val smsGenerator: (Int) -> SenderWithSms = { i ->
        val sender = Sender("Singtel", null)
        val sms = listOf(
            SmsData(
                "<ADV> Some super cool proposal specially for you!",
                LocalDateTime.now().minusDays(i.toLong())
            )
        )
        SenderWithSms(sender, sms)
    }

    MainView(modifier = Modifier, List(5) { smsGenerator(it) })
}

data class SenderWithSms(val sender: Sender, val sms: List<SmsData>)
data class SmsData(val message: String, val timestamp: LocalDateTime) {
    fun formatTimestamp(): String {
        val today = LocalDate.now()

        return if (today == timestamp.toLocalDate()) {
            "${timestamp.hour}:${if(timestamp.minute < 10) "0${timestamp.minute}" else timestamp.minute}"
        } else {
            "${timestamp.dayOfMonth} ${
                timestamp.month.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    java.util.Locale.getDefault()
                )
            }"
        }
    }
}

data class Sender(val name: String, val image: String?)
