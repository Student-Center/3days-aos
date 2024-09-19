package com.weave.design_system.rule

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DaysShadowValue(
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp,
    val blur: Dp = 0.dp,
    val shadowColor: Color = Color.Black.copy(alpha = 0.08f)
)

@Immutable
data class DaysShadow(
    val default: DaysShadowValue = DaysShadowValue(
        offsetY = 4.dp,
        blur = 8.dp
    ),

    val alert: DaysShadowValue = DaysShadowValue(
        offsetY = 4.dp,
        blur = 8.dp,
        shadowColor = Color(android.graphics.Color.parseColor("#F2597F")).copy(alpha = 0.08f)
    )
)

val Local3DaysShadow = staticCompositionLocalOf { DaysShadow() }
