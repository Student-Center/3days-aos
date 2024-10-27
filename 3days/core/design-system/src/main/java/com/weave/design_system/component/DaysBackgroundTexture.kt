package com.weave.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.weave.design_system.DaysTheme
import com.weave.design_system.R

@Composable
fun DaysBackgroundTextureImage(
    modifier: Modifier = Modifier,
    color: Color = DaysTheme.colors.bgDefault,
) {
    Image(
        modifier = modifier
            .fillMaxSize()
            .background(color),
        painter = painterResource(id = R.drawable.texture_bg),
        contentDescription = stringResource(id = R.string.background_description)
    )
}