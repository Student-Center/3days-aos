package com.weave.home.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow

@Composable
fun WidgetInformation() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .applyShadow(
                shape = RoundedCornerShape(60.dp),
                shadowType = DaysTheme.shadow.default.copy(
                    shadowColor = Color(0xFF5E9BF7).copy(
                        alpha = 0.1f
                    )
                )
            )
            .background(
                shape = RoundedCornerShape(60.dp),
                color = Color(0xFFF2F9FF)
            )
            .border(
                shape = RoundedCornerShape(60.dp),
                width = 1.dp,
                color = Color(0xFF5E9BF7)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "프로필 위젯을 추가해 나를 더 소개해보세요!\uD83D\uDE4C",
            style = DaysTheme.typography.semiBold14.toTextStyle(),
            color = DaysTheme.colors.blue300,
            modifier = Modifier.padding(vertical = 18.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WidgetInformationPreview() {
    Box(
        modifier = Modifier.size(width = 375.dp, height = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        WidgetInformation()
    }
}