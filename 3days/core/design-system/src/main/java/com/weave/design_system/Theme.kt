package com.weave.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.weave.design_system.foundation.DaysColorScheme
import com.weave.design_system.foundation.DaysTypography
import com.weave.design_system.foundation.Local3DaysColorScheme
import com.weave.design_system.foundation.Local3DaysContentColor
import com.weave.design_system.foundation.Local3DaysTypography

@Composable
fun DaysTheme(
    theme: DaysTheme = DaysTheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        Local3DaysColorScheme provides theme.colors,
        Local3DaysTypography provides theme.typography,
        Local3DaysContentColor provides theme.contentColor,
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

    val contentColor: Color
        @Composable
        @ReadOnlyComposable
        get() = Local3DaysContentColor.current
}