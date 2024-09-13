package com.weave.design_system.rule

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val minBorderWidth = 1.dp
internal val normalBorderWidth = 4.dp

enum class DaysBorder(val dp: Dp) {
    Thin(minBorderWidth),
    Normal(normalBorderWidth)
    // ...
}