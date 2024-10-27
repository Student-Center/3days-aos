package com.weave.design_system.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.utils.Keyboard

enum class BtnType {
    Tall, Short
}

@Composable
fun DaysNextButton(
    modifier: Modifier = Modifier,
    message: String = "다음",
    type: BtnType = BtnType.Tall,
    isEnabled: Boolean = false,
    onDisabledClick: () -> Unit = {},
    onEnabledClick: () -> Unit
) {
    val buttonHeight = when (type) {
        BtnType.Tall -> 90.dp
        BtnType.Short -> 68.dp
    }

    Button(
        onClick = {
            if (isEnabled) onEnabledClick() else onDisabledClick()
        },
        enabled = true,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = ButtonColors(
            containerColor = if (isEnabled) DaysTheme.colors.grey500 else DaysTheme.colors.grey100,
            contentColor = if (isEnabled) DaysTheme.colors.white else DaysTheme.colors.white,
            disabledContainerColor = DaysTheme.colors.grey100,
            disabledContentColor = DaysTheme.colors.white,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
    ) {
        Text(
            text = message,
            style = DaysTheme.typography.semiBold18.toTextStyle(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    isKeyboardVisible: Keyboard,
    isEnabled: Boolean,
    padding: PaddingValues,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        DaysNextButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = if (isKeyboardVisible == Keyboard.Closed) {
                        padding.calculateBottomPadding()
                    } else {
                        0.dp
                    }
                ),
            message = stringResource(id = R.string.next_button_message),
            type = if (isKeyboardVisible == Keyboard.Opened) BtnType.Short else BtnType.Tall,
            isEnabled = isEnabled,
            onEnabledClick = onClick,
            onDisabledClick = onClick
        )
    }
}