package com.weave.design_system.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlin.math.round


@Composable
fun Float.pxToDp(): Float = round(this/ LocalContext.current.resources.displayMetrics.density)

@Composable
fun Float.dpToPx(): Int = round(this * LocalContext.current.resources.displayMetrics.density).toInt()