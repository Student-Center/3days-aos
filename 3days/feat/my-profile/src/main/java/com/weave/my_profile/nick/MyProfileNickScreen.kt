package com.weave.my_profile.nick

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.addFocusCleaner
import com.weave.my_profile.MyProfileSharedViewModel
import com.weave.my_profile.R
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.weave.design_system.R as design

@Composable
fun MyProfileNickScreen(
    viewModel: MyProfileNickViewModel = hiltViewModel(),
    sharedViewModel: MyProfileSharedViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by keyboardAsState()
    val snackBarPadding = if (isKeyboardVisible == Keyboard.Closed) 110.dp else 36.dp
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (sharedViewModel.nickname.isNotEmpty()) {
            viewModel.setAction(
                NickAction.SetNickName(sharedViewModel.nickname)
            )
        }
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is NickEffect.NavigateToNextScreen -> {
                    sharedViewModel.nickname = viewModel.uiState.nickname
                    onNextBtnClicked()
                }

                is NickEffect.ShowToast -> scope.launch {
                    val job = launch {
                        snackState.showSnackbar(
                            message = effect.message,
                            actionLabel = effect.type.toString(),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(3000L)
                    job.cancel()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysOnlyBackAppbar(onBackPressed = onBackBtnClicked)
        },
        snackbarHost = {
            DaysSnackBarHost(
                snackState = snackState,
                modifier = Modifier
                    .padding(bottom = snackBarPadding)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            DaysBackgroundTextureImage()

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 26.dp)
            ) {
                NickNameHeader()

                Spacer(modifier = Modifier.height(48.dp))

                NickNameTextField(
                    focusManager = focusManager,
                    nickname = viewModel.uiState.nickname,
                    nicknameChanged = { viewModel.setAction(NickAction.SetNickName(it)) }
                )
            }

            NextButton(
                isKeyboardVisible = isKeyboardVisible,
                useGradient = DaysTheme.colors.gradientA,
                message = "확인",
                isEnabled = viewModel.uiState.nickname.length >= 2,
                padding = innerPadding,
                onClick = {
                    viewModel.setAction(NickAction.ValidateNickState)
                }
            )
        }
    }
}

@Composable
private fun NickNameTextField(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    nickname: String,
    nicknameChanged: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 18.dp)
            .imePadding(),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        )
        {
            Image(
                modifier = Modifier.aspectRatio(227 / 138f),
                painter = painterResource(id = R.drawable.png_nick_bg),
                contentDescription = ""
            )
        }

        BasicTextField(
            modifier = modifier
                .matchParentSize(),
            value = nickname,
            onValueChange = { newValue ->
                val regex = Regex("^[가-힣ㆍᆞᆢㄱ-ㅎㅏ-ㅣ]*$")
                if (newValue.isEmpty() || regex.matches(newValue)) {
                    nicknameChanged(newValue.replace(" ", ""))
                }
            },
            textStyle = DaysTheme.typography.regular14.copy(
                fontSize = 28.dp,
                lineHeight = 42.dp,
                textAlign = TextAlign.Center,
                color = DaysTheme.colors.grey500
            ).toTextStyle(),
            cursorBrush = SolidColor(DaysTheme.colors.grey500),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, end = 42.dp, bottom = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun NickNameHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = design.string.my_profile_nick_sub_title),
            style = DaysTheme.typography.regular14.toTextStyle(),
            color = DaysTheme.colors.grey200
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(id = design.string.my_profile_nick_title),
            style = DaysTheme.typography.semiBold24.toTextStyle(),
            color = DaysTheme.colors.grey500
        )
    }
}

@Preview
@Composable
private fun MyProfileNickScreenPreview() {
    MyProfileNickScreen(
        onBackBtnClicked = {},
        onNextBtnClicked = {}
    )
}