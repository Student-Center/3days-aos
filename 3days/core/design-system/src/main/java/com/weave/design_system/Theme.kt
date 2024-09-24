package com.weave.design_system

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.weave.design_system.foundation.DaysColorScheme
import com.weave.design_system.foundation.DaysTypography
import com.weave.design_system.foundation.Local3DaysColorScheme
import com.weave.design_system.foundation.Local3DaysTypography
import com.weave.design_system.rule.DaysShadow
import com.weave.design_system.rule.Local3DaysShadow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaysTheme(
    theme: DaysTheme = DaysTheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        Local3DaysColorScheme provides theme.colors,
        Local3DaysTypography provides theme.typography,
        Local3DaysShadow provides theme.shadow,
        LocalOverscrollConfiguration provides null,
    ) {
        ProvideTextStyle(value = theme.typography.medium14.toTextStyle()) {
            content()
        }
    }
}

object DaysTheme {
    val colors: DaysColorScheme
        @Composable
        @ReadOnlyComposable
        get() = Local3DaysColorScheme.current

    val typography: DaysTypography
        @Composable
        @ReadOnlyComposable
        get() = Local3DaysTypography.current

    val shadow: DaysShadow
        @Composable
        @ReadOnlyComposable
        get() = Local3DaysShadow.current
}