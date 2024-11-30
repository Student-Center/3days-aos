package com.weave.home.profile.main.date.age

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysEditTopBar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.NextButton
import com.weave.design_system.component.Picker
import com.weave.design_system.component.PickerState
import com.weave.design_system.component.rememberPickerState
import com.weave.design_system.component.tooltip.DaysTooltip
import com.weave.design_system.component.tooltip.TooltipDirection
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.profile.UserInfo
import com.weave.home.profile.main.SnackBarViewModel
import com.weave.utils.Keyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Year

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartnerAgeScreen(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    viewModel: EditPartnerAgeViewModel = hiltViewModel(),
    userInfo: UserInfo,
    navigateToProfile: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    var isStartTextFieldFocus by remember { mutableStateOf(false) }
    var isEndTextFieldFocus by remember { mutableStateOf(false) }
    val itemsEndPickerState = rememberPickerState()
    val itemsStartPickerState = rememberPickerState()
    val tooltipState = remember { TooltipState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setAction(EditPartnerAgeAction.FetchData(userInfo))
        userInfo.desiredPartner?.birthYearRange?.let {
            itemsStartPickerState.selectedItem = it.start?.toString() ?: "상관없어요"
            itemsEndPickerState.selectedItem = it.end?.toString() ?: "상관없어요"
        }
    }

    LaunchedEffect(isStartTextFieldFocus, isEndTextFieldFocus) {
        scrollState.animateScrollTo(if (isStartTextFieldFocus || isEndTextFieldFocus) scrollState.maxValue / 2 else 0)
    }

    LaunchedEffect(itemsEndPickerState.selectedItem) {
        if (isEndTextFieldFocus) viewModel.setAction(
            EditPartnerAgeAction.SetUpperAge(
                itemsEndPickerState.selectedItem
            )
        )
    }

    LaunchedEffect(itemsStartPickerState.selectedItem) {
        if (isStartTextFieldFocus) viewModel.setAction(
            EditPartnerAgeAction.SetUnderAge(
                itemsStartPickerState.selectedItem
            )
        )
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EditPartnerAgeEffect.NavigateToProfile -> {
                    navigateToProfile(true)
                }

                is EditPartnerAgeEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    EditPartnerAgeScreenContent(
        uiState = viewModel.uiState,
        snackState = snackBarViewModel.snackBarHostState,
        scrollState = scrollState,
        tooltipState = tooltipState,
        scope = scope,
        itemsStartPickerState = itemsStartPickerState,
        itemsEndPickerState = itemsEndPickerState,
        isStartTextFieldFocus = isStartTextFieldFocus,
        isEndTextFieldFocus = isEndTextFieldFocus,
        onStartTextFieldFocusChange = { isStartTextFieldFocus = it },
        onEndTextFieldFocusChange = { isEndTextFieldFocus = it },
        onStartValueChange = { viewModel.setAction(EditPartnerAgeAction.SetUnderAge(it)) },
        onEndValueChange = { viewModel.setAction(EditPartnerAgeAction.SetUpperAge(it)) },
        navigateToProfile = navigateToProfile,
        requestUpdate = { viewModel.setAction(EditPartnerAgeAction.UpdateData) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPartnerAgeScreenContent(
    uiState: EditPartnerAgeState,
    snackState: SnackbarHostState,
    scrollState: ScrollState,
    tooltipState: TooltipState,
    scope: CoroutineScope,
    itemsStartPickerState: PickerState,
    itemsEndPickerState: PickerState,
    isStartTextFieldFocus: Boolean,
    isEndTextFieldFocus: Boolean,
    onStartTextFieldFocusChange: (Boolean) -> Unit,
    onEndTextFieldFocusChange: (Boolean) -> Unit,
    onStartValueChange: (String) -> Unit,
    onEndValueChange: (String) -> Unit,
    navigateToProfile: (Boolean) -> Unit,
    requestUpdate: () -> Unit
) {
    val ageRange = (0..15).map { it.toString() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysEditTopBar(
                title = "선호 연령 수정",
                onBackPressed = { navigateToProfile(false) }
            )
        },
        snackbarHost = {
            DaysSnackBarHost(
                snackState = snackState,
                modifier = Modifier
                    .padding(bottom = 110.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            DaysBackgroundTextureImage()

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(41.dp))

                PartnerAgeTextField(
                    isUnderType = false,
                    value = itemsEndPickerState.selectedItem,
                    isFocus = isEndTextFieldFocus,
                    onValueChange = onEndValueChange,
                    onFocusChange = onEndTextFieldFocusChange
                )

                Spacer(modifier = Modifier.height(64.dp))

                PartnerAgeTextField(
                    isUnderType = true,
                    value = itemsStartPickerState.selectedItem,
                    isFocus = isStartTextFieldFocus,
                    onValueChange = onStartValueChange,
                    onFocusChange = onStartTextFieldFocusChange
                )

                Spacer(modifier = Modifier.height(30.dp))

                BirthYearTooltip(
                    scope = scope,
                    tooltipState = tooltipState
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            if (isEndTextFieldFocus) {
                AgeRangePicker(ageRange, itemsEndPickerState)
            }

            if (isStartTextFieldFocus) {
                AgeRangePicker(ageRange, itemsStartPickerState)
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = uiState.initAgeRange != uiState.ageRange,
                padding = innerPadding,
                onClick = requestUpdate
            )
        }
    }
}

@Composable
private fun AgeRangePicker(items: List<String>, itemsPickerState: PickerState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    Color.White.copy(alpha = 0.5f),
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(start = 24.dp, end = 24.dp, bottom = 113.dp)
        ) {
            Picker(
                state = itemsPickerState,
                items = items,
                visibleItemsCount = 7,
                listStartIndex = items.indexOf(itemsPickerState.selectedItem).takeIf { it != -1 }
                    ?: 0,
                textStyle = DaysTheme.typography.regular14.copy(fontSize = 22.dp).toTextStyle()
            )
        }
    }
}

@Composable
private fun PartnerAgeTextField(
    isUnderType: Boolean,
    value: String,
    isFocus: Boolean,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit
) {
    val containerColor = if (isUnderType) DaysTheme.colors.pink50 else DaysTheme.colors.green50
    val borderColor = if (isUnderType) Color(0xFFE6B1C4) else Color(0xFFA1BA91)
    val textColor = if (isUnderType) DaysTheme.colors.pink500 else DaysTheme.colors.green500
    val shape = RoundedCornerShape(16.dp)

    val icon = if (isUnderType) R.drawable.ic_pointing_down else R.drawable.ic_pointing_up
    val text = buildAnnotatedString {
        append("내 나이보다 ")
        withStyle(style = SpanStyle(color = textColor)) { append(if (isUnderType) "아래" else "위") }
        append("로")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = DaysTheme.typography.semiBold18.toTextStyle(),
                color = DaysTheme.colors.grey300
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                modifier = Modifier
                    .applyShadow(shape)
                    .onFocusChanged { focusState -> onFocusChange(focusState.isFocused) },
                value = if (value == "상관없어요") "-" else value,
                onValueChange = onValueChange,
                textStyle = DaysTheme.typography.semiBold28.copy(
                    fontSize = 36.dp,
                    textAlign = TextAlign.Center,
                    color = textColor
                ).toTextStyle(),
                readOnly = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .size(width = 91.dp, height = 62.dp)
                            .background(containerColor, shape)
                            .border(
                                width = 4.dp,
                                color = if (isFocus) borderColor else DaysTheme.colors.white,
                                shape = shape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "-",
                                style = DaysTheme.typography.semiBold28.copy(
                                    fontSize = 36.dp,
                                    textAlign = TextAlign.Center,
                                    color = textColor.copy(alpha = 0.4f)
                                ).toTextStyle()
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "살까지 괜찮아요",
                style = DaysTheme.typography.semiBold18.toTextStyle(),
                color = DaysTheme.colors.grey300
            )
        }
    }
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
@Preview
@Composable
private fun EditPartnerAgeScreenPreview() {
    val scrollState = rememberScrollState()
    var isStartTextFieldFocus by remember { mutableStateOf(false) }
    var isEndTextFieldFocus by remember { mutableStateOf(false) }
    val itemsEndPickerState = rememberPickerState()
    val itemsStartPickerState = rememberPickerState()
    val tooltipState = remember { TooltipState() }
    val scope = rememberCoroutineScope()

    EditPartnerAgeScreenContent(
        uiState = EditPartnerAgeState(),
        snackState = SnackbarHostState(),
        scrollState = scrollState,
        navigateToProfile = { },
        tooltipState = tooltipState,
        scope = scope,
        itemsStartPickerState = itemsStartPickerState,
        itemsEndPickerState = itemsEndPickerState,
        isStartTextFieldFocus = isStartTextFieldFocus,
        isEndTextFieldFocus = isEndTextFieldFocus,
        onStartTextFieldFocusChange = { isStartTextFieldFocus = it },
        onEndTextFieldFocusChange = { isEndTextFieldFocus = it },
        onStartValueChange = { },
        onEndValueChange = { },
        requestUpdate = { }
    )
}