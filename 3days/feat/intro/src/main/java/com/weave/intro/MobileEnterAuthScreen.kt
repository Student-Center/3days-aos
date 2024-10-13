package com.weave.intro

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBar
import com.weave.design_system.component.SnackBarType
import com.weave.design_system.extension.addFocusCleaner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MobileEnterAuthScreen(
    viewModel: MobileEnterAuthViewModel = hiltViewModel(),
    mobileNum: String,
    onBackBtnClicked: () -> Unit,
    navigateToMainScreen: () -> Unit,
    navigateToRegisterFlow: (String) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val snackState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState

    // SMS retriever 시작/중지
    DisposableEffect(LocalLifecycleOwner.current) {
        (context as? Activity)?.let {
            viewModel.startSmsRetriever(it)
            viewModel.setAction(AuthAction.RequestVerificationCode(mobileNum))
        }
        onDispose { (context as? Activity)?.let { viewModel.stopSmsRetriever() } }
    }

    // UI 효과 처리
    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is AuthEffect.AutoFillCodeEffect -> {
                    effect.code.forEachIndexed { index, item ->
                        uiState.code[index] = item.toString()
                    }
                    if (uiState.code.all { it.isNotBlank() }) viewModel.setAction(
                        AuthAction.VerifyCode(
                            uiState.code.joinToString("")
                        )
                    )
                }

                is AuthEffect.NavigateToMainScreen -> navigateToMainScreen()
                is AuthEffect.NavigateToRegisterFlow -> {
                    navigateToRegisterFlow(effect.registerToken)
                }

                is AuthEffect.ShowToast -> coroutineScope.launch {
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
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .imePadding(),
        topBar = { DaysOnlyBackAppbar(onBackPressed = onBackBtnClicked) },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .padding(bottom = 20.dp)
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
        Box(modifier = Modifier.fillMaxSize()) {
            // 배경 이미지 설정
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DaysTheme.colors.bgDefault),
                painter = painterResource(id = R.drawable.texture_bg),
                contentDescription = stringResource(id = R.string.background_description)
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

                // 타이틀 텍스트
                Text(
                    text = stringResource(id = R.string.mobile_enter_title),
                    style = DaysTheme.typography.semiBold24.toTextStyle(),
                    color = DaysTheme.colors.grey500
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 인증 코드 입력 Row
                CodeInputRow(
                    focusManager = focusManager,
                    code = uiState.code,
                    onCodeChanged = { index, value ->
                        uiState.code[index] = value
                        if (uiState.code.all { it.isNotBlank() }) viewModel.setAction(
                            AuthAction.VerifyCode(
                                uiState.code.joinToString("")
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 코드 재전송 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                color = Color(0x0F454545),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                (context as? Activity)?.let { viewModel.startSmsRetriever(it) }

                                viewModel.setAction(AuthAction.RequestVerificationCode(mobileNum))
                                uiState.code.forEachIndexed { index, _ -> uiState.code[index] = "" }
                            }
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.mobile_enter_resend,
                                formatPhoneNumber(mobileNum)
                            ),
                            style = DaysTheme.typography.regular14.toTextStyle(),
                            color = DaysTheme.colors.grey300
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CodeInputRow(
    focusManager: FocusManager,
    code: SnapshotStateList<String>,
    onCodeChanged: (index: Int, value: String) -> Unit
) {
    val focusRequesters = List(6) { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(323f / 72f),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        code.forEachIndexed { index, _ ->
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFF7F3F1), RoundedCornerShape(14.dp))
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged {
                        if (it.isFocused && code[index].isNotEmpty()) {
                            onCodeChanged(index, "")
                        }
                    }
                    .onKeyEvent { event: KeyEvent ->
                        if (event.key == Key.Backspace) {
                            if (code[index].isEmpty()) {
                                if (index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                            return@onKeyEvent true
                        }
                        false
                    },
                textStyle = DaysTheme.typography.semiBold24.copy(
                    textAlign = TextAlign.Center
                ).toTextStyle(),
                value = code[index],
                onValueChange = { newValue ->
                    if (newValue.length == 1 && newValue.all { it.isDigit() }) {
                        onCodeChanged(index, newValue)
                        val emptyIdx = code.indexOfFirst { it.isBlank() }
                        if (emptyIdx != -1) {
                            focusRequesters[emptyIdx].requestFocus()
                        } else {
                            focusManager.clearFocus()
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                maxLines = 1,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) { innerTextField() }
                },
            )
        }
    }
}

fun formatPhoneNumber(input: String): String {
    val regex = "(\\d{3})(\\d{4})(\\d{4})".toRegex()
    return input.replace(regex, "$1-$2-$3")
}

@Preview(showBackground = true)
@Composable
fun MobileEnterAuthScreenPreview() {
    MobileEnterAuthScreen(
        mobileNum = "01012345678",
        onBackBtnClicked = {},
        navigateToMainScreen = {},
        navigateToRegisterFlow = {}
    )
}
