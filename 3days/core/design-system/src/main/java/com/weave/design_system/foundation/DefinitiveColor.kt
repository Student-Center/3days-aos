package com.weave.design_system.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.weave.design_system.DaysTheme

@Immutable
data class DaysColorScheme(
    val black: Color = hexToColor("#1F1F1F"),
    val white: Color = hexToColor("#FFFFFF"),

    val grey500: Color = hexToColor("#454545"),
    val grey400: Color = hexToColor("#5E5E5E"),
    val grey300: Color = hexToColor("#848484"),
    val grey200: Color = hexToColor("#A0A0A0"),
    val grey100: Color = hexToColor("#CAC7C5"),

    val gradientA: List<Color> = listOf(hexToColor("#FF8BAC"), hexToColor("#98C1FF")),

    val red300: Color = hexToColor("#F2597F"),
    val blue300: Color = hexToColor("#408CFF"),

    val darkGreen: Color = hexToColor("#5B6654"),
    val darkPink: Color = hexToColor("#846470"),
    val darkBlue: Color = hexToColor("#606D8F"),

    val lightYellow: Color = hexToColor("#FEFDED"),
    val lightGreen: Color = hexToColor("#F0F6EB"),
    val lightPink: Color = hexToColor("#F6EBEF"),
    val lightBlue: Color = hexToColor("#EBF1F6"),

    val background: Color = hexToColor("#F5F1EE")
)

private val lightColorScheme = DaysColorScheme()

internal fun hexToColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}

val Local3DaysColorScheme = staticCompositionLocalOf { lightColorScheme }
val Local3DaysContentColor = compositionLocalOf { lightColorScheme.black }


// ripple effect
@Composable
@ReadOnlyComposable
fun pressedColorFor(color: Color) = DaysTheme.colors.pressedColorFor(color)

private fun DaysColorScheme.pressedColorFor(color: Color): Color {
    return when (color) {
        grey100 -> grey500
        // Add more color cases as needed 
        else -> color.copy(alpha = 0.7f)
    }
}


