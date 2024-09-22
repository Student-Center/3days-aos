package com.weave.design_system.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.weave.design_system.DaysTheme

@Immutable
data class DaysColorScheme(
    val black: Color = Color(0xFF1F1F1F),
    val white: Color = Color(0xFFFFFFFF),

    val grey500: Color = Color(0xFF454545),
    val grey400: Color = Color(0xFF5E5E5E),
    val grey300: Color = Color(0xFF848484),
    val grey200: Color = Color(0xFFA0A0A0),
    val grey100: Color = Color(0xFFCAC7C5),
    val grey50: Color = Color(0xFFF2F1F1),

    val gradientA: List<Color> = listOf(Color(0xFFFF8BAC), Color(0xFF98C1FF)),

    val yellow50: Color = Color(0xFFFEFDED),
    val green50: Color = Color(0xFFF0F6EB),
    val pink50: Color = Color(0xFFF6EBEF),
    val blue50: Color = Color(0xFFEBF1F6),

    val yellow100: Color = Color(0xFFDFDBA5),
    val green100: Color = Color(0xFFAFCA9A),
    val pink100: Color = Color(0xFFE6B1C4),

    val red300: Color = Color(0xFFF2597F),
    val blue300: Color = Color(0xFF5E9BF7),

    val green500: Color = Color(0xFF5B6654),
    val pink500: Color = Color(0xFF846470),
    val blue500: Color = Color(0xFF606D8F),

    val background: Color = Color(0xFFF5F1EE)
)

val Local3DaysColorScheme = staticCompositionLocalOf { DaysColorScheme() }

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


