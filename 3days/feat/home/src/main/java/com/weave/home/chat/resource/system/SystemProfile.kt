package com.weave.home.chat.resource.system

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow
import com.weave.home.R

@Composable
fun SystemProfile() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .applyShadow(CircleShape)
            .background(
                brush = Brush.linearGradient(listOf(Color(0xFF9FFFE1), Color(0xFFFFA2BC))),
                shape = CircleShape
            )
            .border(
                width = 3.75.dp,
                color = DaysTheme.colors.white,
                shape = CircleShape
            )
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(R.drawable.ic_fairy),
            contentDescription = "",
        )
    }
}

@Preview
@Composable
private fun SystemProfilePreview() {
    SystemProfile()
}