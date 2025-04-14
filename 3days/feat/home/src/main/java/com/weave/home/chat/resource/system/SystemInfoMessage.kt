package com.weave.home.chat.resource.system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.model.domain.chat.Message

@Composable
fun SystemInfoMessage(message: Message) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))
        SystemProfile()
        Spacer(Modifier.height(16.dp))
        Text(
            text = message.content.text ?: "",
            style = DaysTheme.typography.medium14.copy(fontSize = 15.dp).toTextStyle(),
            color = DaysTheme.colors.grey500
        )
        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(
                    color = Color(0x1A534C44)
                )
        )
        Spacer(Modifier.height(10.dp))
    }
}