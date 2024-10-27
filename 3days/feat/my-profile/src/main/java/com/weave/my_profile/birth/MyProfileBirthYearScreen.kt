package com.weave.my_profile.birth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.ImeAction
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
import com.weave.design_system.component.Gender
import com.weave.design_system.component.SnackBarType
import com.weave.design_system.component.tooltip.DaysTooltip
import com.weave.design_system.component.tooltip.TooltipDirection
import com.weave.design_system.extension.addFocusCleaner
import com.weave.design_system.extension.noRippleClickable
import com.weave.my_profile.MyProfileSharedViewModel
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Year

@Composable
fun MyProfileBirthYearScreen(
    modifier: Modifier = Modifier,
    viewModel: MyProfileBirthYearViewModel = hiltViewModel(),
    sharedViewModel: MyProfileSharedViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by keyboardAsState()
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (sharedViewModel.birthYear.all { it.isNotEmpty() }) {
            sharedViewModel.birthYear.forEachIndexed { index, value ->
                viewModel.setAction(BirthYearAction.SetBirthYear(index, value))
            }
        }
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is BirthYearEffect.NavigateToNextScreen -> {
                    repeat(4) {
                        sharedViewModel.birthYear[it] = viewModel.uiState.birthYear[it]
                    }.also {
                        onNextBtnClicked()
                    }
                }

                is BirthYearEffect.ShowToast -> scope.launch {
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

    BirthYearScreenContent(modifier = modifier,
        scope = scope,
        uiState = viewModel.uiState,
        isKeyboardVisible = isKeyboardVisible,
        snackState = snackState,
        focusManager = focusManager,
        genderState = sharedViewModel.genderState,
        onBirthYearChanged = { index, value ->
            viewModel.setAction(BirthYearAction.SetBirthYear(index, value))
        },
        onBackPressed = onBackBtnClicked,
        onNextClicked = {
            viewModel.setAction(BirthYearAction.ValidateBirthYear)
        })
}

@Composable
private fun boldBirthYearMessage(): AnnotatedString {
    return remember {
        val currentYear = Year.now().value
        val twentyYearsOld = currentYear - 20
        val thirtyFiveYearsOld = currentYear - 35

        buildAnnotatedString {
            append("${currentYear}년 기준으로 ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${twentyYearsOld}년생(만 20살)부터 ${thirtyFiveYearsOld}년생(만 35살) ")
            }
            append("까지 가입할 수 있어요")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthYearScreenContent(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    uiState: BirthYearState,
    isKeyboardVisible: Keyboard,
    snackState: SnackbarHostState,
    focusManager: FocusManager,
    genderState: Gender,
    onBirthYearChanged: (Int, String) -> Unit,
    onBackPressed: () -> Unit,
    onNextClicked: () -> Unit
) {
    val tooltipState = remember { TooltipState() }
    val snackBarPadding = if (isKeyboardVisible == Keyboard.Closed) 110.dp else 36.dp

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        DaysOnlyBackAppbar(onBackPressed = onBackPressed)
    }, snackbarHost = {
        DaysSnackBarHost(
            snackState = snackState, modifier = Modifier
                .padding(bottom = snackBarPadding)
                .imePadding()
        )
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            BackgroundImage()

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 26.dp)
            ) {
                BirthYearHeader(
                    oppositeGender = if (genderState == Gender.MALE) Gender.FEMALE.koValue else Gender.MALE.koValue
                )

                Spacer(modifier = Modifier.height(40.dp))

                BirthYearInputSection(
                    birthYear = uiState.birthYear,
                    isInvalidBirthYear = uiState.invalidBirthYearFlag,
                    focusManager = focusManager,
                    onNumChanged = onBirthYearChanged
                )

                Spacer(modifier = Modifier.height(24.dp))

                BirthYearTooltip(
                    tooltipState = tooltipState, scope = scope
                )
            }

            NextButton(
                isKeyboardVisible = isKeyboardVisible,
                isEnabled = uiState.birthYear.all { it.isNotBlank() },
                padding = innerPadding,
                onClick = onNextClicked
            )
        }
    }
}

@Composable
private fun BackgroundImage() {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .background(DaysTheme.colors.bgDefault),
        painter = painterResource(id = R.drawable.texture_bg),
        contentDescription = stringResource(id = R.string.background_description)
    )
}

@Composable
private fun DaysSnackBarHost(
    snackState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackState,
    ) { snackData ->
        DaysSnackBar(
            message = snackData.visuals.message,
            type = if (snackData.visuals.actionLabel == SnackBarType.DEFAULT.toString()) {
                SnackBarType.DEFAULT
            } else {
                SnackBarType.ERROR
            }
        )
    }
}

@Composable
private fun BirthYearHeader(
    oppositeGender: String
) {
    Spacer(modifier = Modifier.height(12.dp))

    DaysStepIndicator(currentStep = 2, totalStep = 5)

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = stringResource(
            id = R.string.my_profile_birth_year_sub_title, oppositeGender
        ), style = DaysTheme.typography.regular14.toTextStyle(), color = DaysTheme.colors.grey200
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = stringResource(id = R.string.my_profile_birth_year_title),
        style = DaysTheme.typography.semiBold24.toTextStyle(),
        color = DaysTheme.colors.grey500
    )
}

@Composable
fun BirthYearInputSection(
    birthYear: List<String>,
    isInvalidBirthYear: Boolean,
    focusManager: FocusManager,
    onNumChanged: (Int, String) -> Unit
) {
    val focusRequesters = List(4) { FocusRequester() }
    val isFocused = remember { mutableIntStateOf(-1) }
    val showHint = birthYear.all { it.isEmpty() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(314f / 92f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        birthYear.forEachIndexed { index, _ ->
            BirthYearDigitInput(modifier = Modifier.weight(1f),
                index = index,
                value = birthYear[index],
                isInvalidBirthYear = isInvalidBirthYear,
                isFocused = isFocused.intValue == index,
                showHint = showHint,
                focusRequester = focusRequesters[index],
                focusManager = focusManager,
                onFocusChanged = { focused ->
                    if (focused) {
                        isFocused.intValue = index
                        onNumChanged(index, "")
                    } else if (isFocused.intValue == index) {
                        isFocused.intValue = -1
                    }
                },
                onValueChanged = { newValue ->
                    if (newValue.length == 1 && newValue.all { it.isDigit() }) {
                        onNumChanged(index, newValue)
                        val nextIdx = index + 1
                        if (nextIdx < birthYear.size) {
                            focusRequesters[nextIdx].requestFocus()
                        } else {
                            focusManager.clearFocus()
                        }
                    }
                },
                onBackspace = {
                    if (birthYear[index].isEmpty() && index > 0) {
                        focusRequesters[index - 1].requestFocus()
                    }
                })
        }

        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = "년생",
            style = DaysTheme.typography.semiBold18.toTextStyle(),
            color = DaysTheme.colors.grey300
        )
    }
}

@Composable
private fun BirthYearDigitInput(
    modifier: Modifier = Modifier,
    index: Int,
    value: String,
    isInvalidBirthYear: Boolean,
    isFocused: Boolean,
    showHint: Boolean,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    onFocusChanged: (Boolean) -> Unit,
    onValueChanged: (String) -> Unit,
    onBackspace: () -> Unit
) {
    BasicTextField(modifier = modifier
        .fillMaxSize()
        .background(
            color = if (isInvalidBirthYear) DaysTheme.colors.pink50 else DaysTheme.colors.yellow50,
            shape = RoundedCornerShape(20.dp)
        )
        .border(
            width = 4.dp, color = when {
                isInvalidBirthYear -> DaysTheme.colors.red300
                isFocused -> Color(0xFFDFDBA5)
                else -> DaysTheme.colors.white
            }, shape = RoundedCornerShape(20.dp)
        )
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            onFocusChanged(focusState.isFocused)
        }
        .onKeyEvent { event: KeyEvent ->
            if (event.key == Key.Backspace) {
                onBackspace()
                true
            } else false
        },
        value = value,
        onValueChange = onValueChanged,
        textStyle = DaysTheme.typography.semiBold28.copy(
            fontSize = 40.dp, lineHeight = 60.dp, textAlign = TextAlign.Center
        ).toTextStyle(),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
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
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthYearTooltip(
    tooltipState: TooltipState, scope: CoroutineScope
) {
    DaysTooltip(tooltipState = tooltipState,
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
                    contentDescription = stringResource(id = R.string.my_profile_birth_year_tooltip_description)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = stringResource(id = R.string.my_profile_birth_year_description),
                    style = DaysTheme.typography.regular14.toTextStyle(),
                    color = DaysTheme.colors.grey200
                )
            }
        })
}

@Composable
private fun NextButton(
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

@Preview
@Composable
fun MyProfileBirthYearScreenPreview() {
    MyProfileBirthYearScreen(onNextBtnClicked = {}, onBackBtnClicked = {})
}