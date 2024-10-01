package com.weave.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.BtnType
import com.weave.design_system.component.DaysNextButton
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysPhoneTextField
import com.weave.design_system.extension.addFocusCleaner
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState

@Composable
fun MobileSendAuthScreen(
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by keyboardAsState()

    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var isEnabled by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .imePadding(),
        topBar = {
            DaysOnlyBackAppbar(onBackPressed = onBackBtnClicked)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DaysTheme.colors.bgDefault),
                painter = painterResource(id = R.drawable.texture_bg),
                contentDescription = stringResource(id = R.string.mobile_send_bg_description)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                        start = 26.dp,
                        end = 26.dp
                    )
            ) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(id = R.string.mobile_send_title),
                    style = DaysTheme.typography.semiBold24.toTextStyle(),
                    color = DaysTheme.colors.grey500
                )

                Spacer(modifier = Modifier.height(30.dp))

                DaysPhoneTextField(
                    focusManager = focusManager,
                    phoneNumber = phoneNumber,
                    isValid = { isValid ->
                        isEnabled = isValid
                    },
                    onTextChange = { newText ->
                        if(newText.text.all { it.isDigit() }){
                            phoneNumber = newText
                        }
                    }
                )
            }

            DaysNextButton(
                message = stringResource(id = R.string.next_button_message),
                type = if (isKeyboardVisible == Keyboard.Opened) BtnType.Short else BtnType.Tall,
                isEnabled = isEnabled,
                onClick = { onNextBtnClicked(phoneNumber.text) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = if (isKeyboardVisible == Keyboard.Closed) innerPadding.calculateBottomPadding() else 0.dp
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MobileAuthScreenPreview() {
    MobileSendAuthScreen(
        onBackBtnClicked = {},
        onNextBtnClicked = {}
    )
}
