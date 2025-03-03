package com.weave.home.chat.resource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.utils.DateTimeUtil

@Composable
fun DayDivider(
    value: String
) {
    Column {
        Spacer(Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Box(
                Modifier
                    .weight(1f)
                    .height(0.5.dp)
                    .background(Color(0x1A534C44))
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = DateTimeUtil.formatDayDivider(value),
                style = DaysTheme.typography.regular12.toTextStyle(),
                color = Color(0x80534C44)
            )

            Spacer(Modifier.width(10.dp))

            Box(
                Modifier
                    .weight(1f)
                    .height(0.5.dp)
                    .background(Color(0x1A534C44))
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
private fun DayDividerPreview() {
    DayDivider("2025-02-23T11:40:52.743333")
}