package com.weave.my_profile

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.BtnType
import com.weave.design_system.component.DaysGenderSelector
import com.weave.design_system.component.DaysNextButton
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBar
import com.weave.design_system.component.DaysStepIndicator
import com.weave.design_system.component.SnackBarType
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyProfileGenderScreen(
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val isKeyboardVisible by keyboardAsState()
    var isEnabled by remember { mutableStateOf(false) }
    var genderState by remember { mutableStateOf("") }
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val noSelectedMessage = stringResource(id = R.string.my_profile_gender_no_selected_message)

    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding(),
        topBar = {
            DaysOnlyBackAppbar(onBackPressed = onBackBtnClicked)
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .padding(bottom = 110.dp)
                    .imePadding(),
                hostState = snackState,
            ) { snackData ->
                DaysSnackBar(
                    message = snackData.visuals.message,
                    type = if (snackData.visuals.actionLabel == SnackBarType.DEFAULT.toString()) SnackBarType.DEFAULT else SnackBarType.ERROR
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
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
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 26.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                DaysStepIndicator(currentStep = 1, totalStep = 5)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.my_profile_gender_sub_title),
                    style = DaysTheme.typography.regular14.toTextStyle(),
                    color = DaysTheme.colors.grey200
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.my_profile_gender_title),
                    style = DaysTheme.typography.semiBold24.toTextStyle(),
                    color = DaysTheme.colors.grey500
                )

                Spacer(modifier = Modifier.height(40.dp))

                DaysGenderSelector(
                    genderState = genderState,
                    onChangedGender = { newValue ->
                        genderState = newValue
                        isEnabled = genderState.isNotEmpty()
                    }
                )
            }

            DaysNextButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = if (isKeyboardVisible == Keyboard.Closed) innerPadding.calculateBottomPadding() else 0.dp
                    ),
                message = stringResource(id = R.string.next_button_message),
                type = if (isKeyboardVisible == Keyboard.Opened) BtnType.Short else BtnType.Tall,
                isEnabled = isEnabled,
                onEnabledClick = onNextBtnClicked,
                onDisabledClick = {
                    scope.launch {
                        val job = launch {
                            snackState.showSnackbar(
                                message = noSelectedMessage,
                                actionLabel = SnackBarType.ERROR.toString(),
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(3000L)
                        job.cancel()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun MyProfileGenderScreenPreview() {
    MyProfileGenderScreen(
        onNextBtnClicked = {},
        onBackBtnClicked = {}
    )
}