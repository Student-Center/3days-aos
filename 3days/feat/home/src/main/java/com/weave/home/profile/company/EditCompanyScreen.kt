package com.weave.home.profile.company

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.DaysAutoCompleteTextField
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysCheckBox
import com.weave.design_system.component.DaysEditTopBar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.profile.UserInfo
import com.weave.home.profile.main.SnackBarViewModel
import com.weave.model.domain.myprofile.Company
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState

@Composable
fun EditCompanyScreen(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    viewModel: EditCompanyViewModel = hiltViewModel(),
    userInfo: UserInfo,
    navigateToProfile: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by keyboardAsState()
    val lazyListState = rememberLazyListState()
    var showBottomSheetState by remember { mutableStateOf(false) }

    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.setAction(EditCompanyAction.FetchData(userInfo))
    }

    LaunchedEffect(inputText) {
        if (inputText.length >= 2) viewModel.setAction(EditCompanyAction.SearchCompanies(inputText))
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EditCompanyEffect.NavigateToProfile -> {
                    navigateToProfile(effect.isSuccess)
                }

                is EditCompanyEffect.ShowBottomSheet -> {
                    showBottomSheetState = true
                }

                is EditCompanyEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    EditCompanyScreenContent(
        uiState = viewModel.uiState,
        isKeyboardVisible = isKeyboardVisible,
        focusManager = focusManager,
        showBottomSheetState = showBottomSheetState,
        snackState = snackBarViewModel.snackBarHostState,
        lazyListState = lazyListState,
        inputText = inputText,
        onTextChanged = { inputText = it },
        onCheckChanged = {
            viewModel.setAction(EditCompanyAction.SetChecked(!viewModel.uiState.isChecked))
            inputText = ""
        },
        navigateToProfile = {
            navigateToProfile(it)
        },
        onCompanyChanged = {
            viewModel.setAction(EditCompanyAction.SelectCompany(it))
        },
        requestUpdate = {
            viewModel.setAction(EditCompanyAction.ValidateInput)
        },
        onBottomSheetCanceled = { showBottomSheetState = false },
        onClickBottomSheetConfirm = {
            showBottomSheetState = false
            viewModel.setAction(EditCompanyAction.UpdateData(it))
        }
    )
}

@Composable
private fun EditCompanyScreenContent(
    uiState: EditCompanyState,
    isKeyboardVisible: Keyboard,
    focusManager: FocusManager,
    showBottomSheetState: Boolean,
    snackState: SnackbarHostState,
    lazyListState: LazyListState,
    inputText: String,
    onTextChanged: (String) -> Unit,
    onCheckChanged: () -> Unit,
    navigateToProfile: (Boolean) -> Unit,
    onCompanyChanged: (Company) -> Unit,
    requestUpdate: () -> Unit,
    onBottomSheetCanceled: () -> Unit,
    onClickBottomSheetConfirm: (Boolean) -> Unit
) {
    val snackBarPadding = if (isKeyboardVisible == Keyboard.Closed) 110.dp else 36.dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysEditTopBar(
                title = "회사 수정",
                onBackPressed = { navigateToProfile(false) }
            )
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
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            DaysBackgroundTextureImage()

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(34.dp))

                CurrentItem(
                    item = uiState.initCompany
                )

                Spacer(modifier = Modifier.height(32.dp))

                CompanyInputSection(
                    focusManager = focusManager,
                    lazyListState = lazyListState,
                    inputText = inputText,
                    onTextChanged = onTextChanged,
                    companies = uiState.companies,
                    selectedCompany = uiState.selectedCompany,
                    onCompanyChanged = onCompanyChanged,
                )

                Spacer(modifier = Modifier.height(20.dp))

                NotVisibleMyCompanyCheckBox(
                    checkState = uiState.isChecked,
                    onCheckChanged = {
                        focusManager.clearFocus()
                        onCheckChanged()
                    }
                )
            }

            NextButton(
                isKeyboardVisible = isKeyboardVisible,
                isEnabled = uiState.isChecked || uiState.selectedCompany != null,
                padding = innerPadding,
                onClick = requestUpdate
            )
        }

        if (showBottomSheetState) {
            CompanyMatchOptionSheet(
                onClickCancel = onBottomSheetCanceled,
                onClickConfirmTrue = { onClickBottomSheetConfirm(true) },
                onClickConfirmFalse = { onClickBottomSheetConfirm(false) }
            )
        }
    }
}

@Composable
private fun CurrentItem(
    item: Company?
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = DaysTheme.colors.yellow50,
                    shape = RoundedCornerShape(65.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFEDE9C1),
                    shape = RoundedCornerShape(65.dp)
                )
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_others),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "내 회사",
                style = DaysTheme.typography.regular12.toTextStyle(),
                color = DaysTheme.colors.grey400
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item?.name ?: "새회사",
                style = DaysTheme.typography.semiBold14.copy(fontSize = 12.dp).toTextStyle(),
                color = DaysTheme.colors.grey400
            )
        }
    }
}

@Composable
private fun NotVisibleMyCompanyCheckBox(
    modifier: Modifier = Modifier,
    checkState: Boolean,
    onCheckChanged: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 24.dp)
            .background(
                color = Color(0x33CAC7C5),
                shape = RoundedCornerShape(12.dp)
            )
            .noRippleClickable { onCheckChanged() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DaysCheckBox(
            checked = checkState,
            onCheckedChange = onCheckChanged
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(id = R.string.my_profile_company_not_visible_my_company),
            style = DaysTheme.typography.medium14.toTextStyle(),
            color = DaysTheme.colors.grey400
        )
    }
}

@Composable
private fun CompanyInputSection(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    lazyListState: LazyListState,
    inputText: String,
    onTextChanged: (String) -> Unit,
    companies: List<Company> = listOf(),
    selectedCompany: Company?,
    onCompanyChanged: (Company) -> Unit
) {
    DaysAutoCompleteTextField(
        modifier = modifier.padding(horizontal = 24.dp),
        placeholderText = "내 회사 검색",
        inputText = inputText,
        onTextChange = onTextChanged,
        suggestions = companies.map { it.name },
        selectedSuggestion = selectedCompany?.name ?: "",
        onSuggestionSelected = { name ->
            companies.find { it.name == name }?.let { onCompanyChanged(it) }
        },
        focusManager = focusManager,
        lazyListState = lazyListState
    )
}

@Preview
@Composable
private fun EditCompanyScreenPreview() {
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    val inputText by remember { mutableStateOf("") }
    var showBottomSheetState by remember { mutableStateOf(false) }

    EditCompanyScreenContent(
        focusManager = focusManager,
        uiState = EditCompanyState(),
        isKeyboardVisible = Keyboard.Closed,
        snackState = SnackbarHostState(),
        showBottomSheetState = showBottomSheetState,
        lazyListState = lazyListState,
        inputText = inputText,
        onTextChanged = {},
        onCheckChanged = {},
        navigateToProfile = {},
        onCompanyChanged = {},
        requestUpdate = {},
        onBottomSheetCanceled = { showBottomSheetState = false },
        onClickBottomSheetConfirm = {
            showBottomSheetState = false
        }
    )
}