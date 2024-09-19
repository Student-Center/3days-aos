package com.weave.design_system.preview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.rule.Local3DaysShadow

@Composable
fun ShadowPreviewBox(
    modifier: Modifier = Modifier,
    shadowType: com.weave.design_system.rule.DaysShadowValue,
    shadowName: String
) {
    Surface(
        modifier = modifier
            .size(100.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .applyShadow(
                shape = RoundedCornerShape(16.dp),
                shadowType = shadowType
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = shadowName,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DaysShadowPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            24.dp, Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShadowPreviewBox(
            shadowType = Local3DaysShadow.current.default,
            shadowName = "Default"
        )

        ShadowPreviewBox(
            shadowType = Local3DaysShadow.current.alert,
            shadowName = "Alert"
        )
    }
}
