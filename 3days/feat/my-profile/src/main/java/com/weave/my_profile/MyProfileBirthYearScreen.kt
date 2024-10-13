package com.weave.my_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.BtnType
import com.weave.design_system.component.DaysNextButton
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBar
import com.weave.design_system.component.DaysStepIndicator
import com.weave.design_system.component.SnackBarType
import com.weave.design_system.component.tooltip.DaysTooltip
import com.weave.design_system.component.tooltip.TooltipDirection
import com.weave.design_system.extension.addFocusCleaner
import com.weave.design_system.extension.noRippleClickable
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Year

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileBirthYearScreen(
    sharedViewModel: MyProfileSharedViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by keyboardAsState()
    var isEnabled by remember { mutableStateOf(sharedViewModel.birthYear.all { it.isNotEmpty() }) }
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val noInputMessage = stringResource(id = R.string.my_profile_birth_year_empty_input_message)
    val tooltipState = remember { TooltipState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                .addFocusCleaner(focusManager)
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

                DaysStepIndicator(currentStep = 2, totalStep = 5)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.my_profile_birth_year_sub_title, if(sharedViewModel.genderState == "남성") "여성" else "남성"),
                    style = DaysTheme.typography.regular14.toTextStyle(),
                    color = DaysTheme.colors.grey200
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.my_profile_birth_year_title),
                    style = DaysTheme.typography.semiBold24.toTextStyle(),
                    color = DaysTheme.colors.grey500
                )

                Spacer(modifier = Modifier.height(40.dp))

                BirthYearInputRow(
                    focusManager = focusManager,
                    birthYear = sharedViewModel.birthYear,
                    onNumChanged = { index, newValue ->
                        sharedViewModel.birthYear[index] = newValue
                        isEnabled = sharedViewModel.birthYear.all { it.isNotEmpty() }
                    },
                    unSupportedYear = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                DaysTooltip(
                    tooltipState = tooltipState,
                    direction = TooltipDirection.Top,
                    tooltipText = boldBirthYearMessage(),
                    content = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable {
                                    scope.launch { tooltipState.show() }
                                },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.ic_question_mark),
                                tint = DaysTheme.colors.grey200,
                                contentDescription = ""
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = stringResource(id = R.string.my_profile_birth_year_description),
                                style = DaysTheme.typography.regular14.toTextStyle(),
                                color = DaysTheme.colors.grey200
                            )
                        }

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
                                message = noInputMessage,
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

fun boldBirthYearMessage(): AnnotatedString {
    val currentYear = Year.now().value
    val twentyYearsOld = currentYear - 20
    val thirtyFiveYearsOld = currentYear - 35

    return buildAnnotatedString {
        append("${currentYear}년 기준으로 ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${twentyYearsOld}년생(만 20살)부터 ${thirtyFiveYearsOld}년생(만 35살) ")
        }
        append("까지 가입할 수 있어요")
    }
}

@Composable
fun BirthYearInputRow(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    birthYear: SnapshotStateList<String>,
    onNumChanged: (Int, String) -> Unit,
    unSupportedYear: Boolean
) {
    val focusRequesters = List(4) { FocusRequester() }
    val isFocused = remember { mutableIntStateOf(-1) }
    val showHint = birthYear.all { it.isEmpty() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(314f / 92f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        birthYear.forEachIndexed { index, _ ->
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(
                        color = if (unSupportedYear) DaysTheme.colors.pink50 else DaysTheme.colors.yellow50,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 4.dp,
                        color = if (unSupportedYear) {
                            DaysTheme.colors.red300
                        } else {
                            if (isFocused.intValue == index) Color(0xFFDFDBA5) else DaysTheme.colors.white
                        },
                        shape = RoundedCornerShape(20.dp)
                    )
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            isFocused.intValue = index
                            onNumChanged(index, "")
                        } else if (isFocused.intValue == index) {
                            isFocused.intValue = -1
                        }
                    }
                    .onKeyEvent { event: KeyEvent ->
                        if (event.key == Key.Backspace) {
                            if (birthYear[index].isEmpty()) {
                                if (index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                            return@onKeyEvent true
                        }
                        false
                    },
                value = birthYear[index],
                onValueChange = { newValue ->
                    if (newValue.length == 1 && newValue.all { it.isDigit() }) {
                        onNumChanged(index, newValue)

                        val nextIdx = index + 1
                        if (nextIdx < birthYear.size) {
                            focusRequesters[nextIdx].requestFocus()
                        } else if (birthYear.all { it.isNotBlank() }) {
                            focusManager.clearFocus()
                        }
                    }
                },
                textStyle = DaysTheme.typography.semiBold28.copy(
                    fontSize = 40.dp, lineHeight = 60.dp, textAlign = TextAlign.Center
                ).toTextStyle(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (showHint) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "2000"[index].toString(),
                                style = DaysTheme.typography.semiBold28.copy(
                                    color = Color(0xFFF1EFCC),
                                    fontSize = 40.dp,
                                    lineHeight = 60.dp,
                                    textAlign = TextAlign.Center
                                ).toTextStyle()
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = "년생",
            style = DaysTheme.typography.semiBold18.toTextStyle(),
            color = DaysTheme.colors.grey300
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BirthYearInputRowPreview() {
    val focusManager = LocalFocusManager.current
    val birthYear = remember { mutableStateListOf("", "", "", "") }

    val onNumChanged: (Int, String) -> Unit = { index, newValue ->
        birthYear[index] = newValue
    }

    BirthYearInputRow(
        focusManager = focusManager,
        birthYear = birthYear,
        onNumChanged = onNumChanged,
        unSupportedYear = false
    )
}

@Preview
@Composable
fun MyProfileBirthYearScreenPreview() {
    MyProfileBirthYearScreen(
        onNextBtnClicked = {},
        onBackBtnClicked = {}
    )
}