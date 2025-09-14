package com.smalser.cleansms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.smalser.cleansms.ui.state.MainUiState
import com.smalser.cleansms.ui.state.SenderSmsUiState
import com.smalser.cleansms.ui.state.smsGenerator

@Composable
fun MainView(modifier: Modifier, mainUiState: MainUiState) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        mainUiState.senderUiStates.forEach {
            SmsRowPreviewView(Modifier, it)
        }
    }
}

@Composable
fun SmsRowPreviewView(modifier: Modifier, senderWithSms: SenderSmsUiState) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .height(48.dp)
            .fillMaxWidth()
    ) {
        SenderAvatarPlaceholder(modifier = Modifier)
        Column(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .weight(1f)
        ) {
            Text(modifier = Modifier.padding(top = 3.dp),
                fontSize = 15.sp,
                text = senderWithSms.sender.name)
            Text(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .fillMaxWidth(),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = senderWithSms.sms.first().message
            )
        }
        Text(
            modifier = Modifier
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
    MainView(modifier = Modifier, MainUiState(List(10) { smsGenerator(it) }))
}

