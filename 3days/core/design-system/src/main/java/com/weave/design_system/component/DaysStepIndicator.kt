package com.weave.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme

@Composable
fun DaysStepIndicator(
    modifier: Modifier = Modifier,
    currentStep: Int,
    totalStep: Int
) {
    Box(
        modifier = modifier
            .height(27.dp)
            .clip(RoundedCornerShape(34.dp))
            .background(DaysTheme.colors.white.copy(alpha = 0.5f))
            .padding(
                horizontal = 10.dp,
                vertical = 2.dp
            )
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = DaysTheme.colors.blue300)) {
                    append(currentStep.toString())
                }
                withStyle(style = SpanStyle(color = DaysTheme.colors.grey300)) {
                    append("/${totalStep}")
                }
            },
            style = DaysTheme.typography.regular15.toTextStyle()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DaysStepIndicatorPreview() {
    DaysStepIndicator(currentStep = 1, totalStep = 5)
}