package com.weave.design_system.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.takeOrElse
import com.weave.design_system.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.W400),
    Font(R.font.pretendard_medium, FontWeight.W500),
    Font(R.font.pretendard_semi_bold, FontWeight.W600),
    Font(R.font.pretendard_bold, FontWeight.W700),
)

val RobotoSlab = FontFamily(
    Font(R.font.robotoslab_medium, FontWeight.W500),
)

@Immutable
data class DaysTextStyle(
    val fontFamily: FontFamily = Pretendard,
    val fontWeight: FontWeight = FontWeight.Medium,
    val fontSize: Dp = Dp.Unspecified,
    val lineHeight: Dp = Dp.Unspecified,
    val letterSpacing: TextUnit = 0.em,
    val color: Color = Color.Unspecified,
    val textAlign: TextAlign = TextAlign.Start,
) {
    @Composable
    fun toTextStyle() = TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = with(LocalDensity.current) { fontSize.toSp() },
        lineHeight = with(LocalDensity.current) { lineHeight.toSp() },
        letterSpacing = letterSpacing,
        color = color,
        textAlign = textAlign,
    )

    fun merge(other: DaysTextStyle?): DaysTextStyle {
        if (other == null || other == Default) return this
        return DaysTextStyle(
            fontFamily = other.fontFamily,
            fontWeight = other.fontWeight,
            fontSize = this.fontSize.takeOrElse { other.fontSize },
            lineHeight = this.lineHeight.takeOrElse { other.lineHeight },
            letterSpacing = this.letterSpacing,
            color = this.color.takeOrElse { other.color },
            textAlign = other.textAlign,
        )
    }

    companion object {
        @Stable
        val Default = DaysTextStyle()
    }
}

@Immutable
data class DaysTypography(
    val enMedium16: DaysTextStyle = DaysTextStyle(
        fontFamily = RobotoSlab,
        fontWeight = FontWeight.W500,
        lineHeight = 16.dp,
        fontSize = 16.dp,
        letterSpacing = (-0.01).em
    ),

    val enMedium20: DaysTextStyle = DaysTextStyle(
        fontFamily = RobotoSlab,
        fontWeight = FontWeight.W500,
        lineHeight = 20.dp,
        fontSize = 20.dp,
        letterSpacing = (-0.01).em
    ),

    val medium14: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        lineHeight = 21.dp,
        fontSize = 14.dp,
    ),

    val medium16: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        lineHeight = 24.dp,
        fontSize = 16.dp,
    ),

    val regular12: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        lineHeight = 12.dp,
        fontSize = 12.dp,
    ),

    val regular14: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        lineHeight = 21.dp,
        fontSize = 14.dp
    ),

    val regular15: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        lineHeight = (22.5).dp,
        fontSize = 15.dp,
    ),

    val semiBold14: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 21.dp,
        fontSize = 14.dp,
    ),

    val semiBold18: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 27.dp,
        fontSize = 18.dp
    ),

    val semiBold20: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 30.dp,
        fontSize = 20.dp
    ),

    val semiBold24: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 36.dp,
        fontSize = 24.dp,
    ),

    val semiBold28: DaysTextStyle = DaysTextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 42.dp,
        fontSize = 28.dp,
    )
)

val Local3DaysTypography = staticCompositionLocalOf { DaysTypography() }
