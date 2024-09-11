package com.weave.design_system.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.weave.design_system.R

data class DaysTypoType(
    val enMedium16: TextStyle = TextStyle(
        fontFamily = RobotoSlab,
        fontWeight = FontWeight.W500,
        lineHeight = 16.sp,
        fontSize = 16.sp,
        letterSpacing = (-0.01).em
    ),

    val enMedium20: TextStyle = TextStyle(
        fontFamily = RobotoSlab,
        fontWeight = FontWeight.W500,
        lineHeight = 20.sp,
        fontSize = 20.sp,
        letterSpacing = (-0.01).em
    ),

    val medium14: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        lineHeight = 21.sp,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    ),

    val medium14Center: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        lineHeight = 21.sp,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    ),

    val medium16Right: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        textAlign = TextAlign.Right
    ),

    val regular12: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        lineHeight = 12.sp,
        fontSize = 12.sp,
    ),

    val regular14: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        lineHeight = 21.sp,
        fontSize = 14.sp
    ),

    val regular15: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        lineHeight = (22.5).sp,
        fontSize = 15.sp,
    ),

    val semiBold14: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 21.sp,
        fontSize = 14.sp,
    ),

    val semiBold18: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        lineHeight = 27.sp,
        fontSize = 18.sp
    ),

    val semiBold24: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 36.sp,
        fontSize = 24.sp,
    ),

    val semiBold28: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        lineHeight = 42.sp,
        fontSize = 28.sp,
    )
)

// Custom Font Style
val DaysTypography = DaysTypoType()

// Material Typography
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)