package com.weave.my_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.BtnType
import com.weave.design_system.component.DaysNextButton
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState

@Composable
fun MyProfileInitScreen(
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val isKeyboardVisible by keyboardAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysOnlyBackAppbar(onBackPressed = onBackBtnClicked)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DaysTheme.colors.bgDefault),
                painter = painterResource(id = R.drawable.texture_bg),
                contentDescription = stringResource(id = R.string.background_description)
            )

            Column(
                modifier = Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(54.dp))
                Text(
                    text = stringResource(id = R.string.my_profile_init_title),
                    style = DaysTheme.typography.semiBold24.toTextStyle(),
                    color = DaysTheme.colors.grey500,
                    textAlign = TextAlign.Center
                )
            }

            DaysNextButton(
                message = stringResource(id = R.string.next_button_message),
                type = if (isKeyboardVisible == Keyboard.Opened) BtnType.Short else BtnType.Tall,
                isEnabled = true,
                onClick = { onNextBtnClicked() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = if (isKeyboardVisible == Keyboard.Closed) innerPadding.calculateBottomPadding() else 0.dp
                    )
            )
        }
    }
}

@Preview
@Composable
fun MyProfileInitScreenPreview() {
    MyProfileInitScreen(
        onBackBtnClicked = {},
        onNextBtnClicked = {}
    )
}