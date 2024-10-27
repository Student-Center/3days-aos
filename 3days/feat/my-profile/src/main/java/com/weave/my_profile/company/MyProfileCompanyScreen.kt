package com.weave.my_profile.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.DaysAutoCompleteTextField
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysCheckBox
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.DaysStepIndicator
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.addFocusCleaner
import com.weave.design_system.extension.noRippleClickable
import com.weave.model.domain.myprofile.Company
import com.weave.my_profile.MyProfileSharedViewModel
import com.weave.utils.Keyboard
import com.weave.utils.keyboardAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyProfileCompanyScreen(
    viewModel: MyProfileCompanyViewModel = hiltViewModel(),
    sharedViewModel: MyProfileSharedViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by keyboardAsState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val snackState = remember { SnackbarHostState() }
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        sharedViewModel.company?.let {
            viewModel.setAction(
                CompanyAction.SelectCompany(sharedViewModel.company!!)
            )
        }
    }

    LaunchedEffect(inputText) {
        if (inputText.length >= 2) viewModel.setAction(CompanyAction.SearchCompanies(inputText))
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CompanyEffect.ShowToast -> scope.launch {
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

    CompanyScreenContent(
        uiState = viewModel.uiState,
        isKeyboardVisible = isKeyboardVisible,
        snackState = snackState,
        focusManager = focusManager,
        lazyListState = lazyListState,
        inputText = inputText,
        onTextChanged = { inputText = it },
        checkState = viewModel.uiState.isChecked,
        onCheckChanged = {
            viewModel.setAction(CompanyAction.SetChecked(!viewModel.uiState.isChecked))
            inputText = ""
        },
        selectedCompany = viewModel.uiState.selectedCompany,
        companies = viewModel.uiState.companies,
        onCompanyChanged = {
            viewModel.setAction(CompanyAction.SelectCompany(it))
        },
        onBackBtnClicked = onBackBtnClicked,
        onNextBtnClicked = {
            if (viewModel.uiState.isChecked || viewModel.uiState.selectedCompany != null) {
                sharedViewModel.company = viewModel.uiState.selectedCompany
                onNextBtnClicked()
            }
        }
    )
}

@Composable
private fun CompanyScreenContent(
    modifier: Modifier = Modifier,
    uiState: CompanyState,
    isKeyboardVisible: Keyboard,
    focusManager: FocusManager,
    lazyListState: LazyListState,
    inputText: String,
    onTextChanged: (String) -> Unit,
    snackState: SnackbarHostState,
    checkState: Boolean,
    onCheckChanged: () -> Unit,
    selectedCompany: Company?,
    companies: List<Company>,
    onCompanyChanged: (Company) -> Unit,
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit,
) {
    val snackBarPadding = if (isKeyboardVisible == Keyboard.Closed) 110.dp else 36.dp

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DaysOnlyBackAppbar(onBackPressed = onBackBtnClicked)
        },
        snackbarHost = {
            DaysSnackBarHost(
                snackState = snackState,
                modifier = Modifier
                    .padding(bottom = snackBarPadding)
                    .imePadding()
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
                CompanyHeader()

                Spacer(modifier = Modifier.height(40.dp))

                CompanyInputSection(
                    focusManager = focusManager,
                    lazyListState = lazyListState,
                    inputText = inputText,
                    onTextChanged = onTextChanged,
                    companies = companies,
                    selectedCompany = selectedCompany,
                    onCompanyChanged = onCompanyChanged,
                )

                Spacer(modifier = Modifier.height(20.dp))

                NotVisibleMyCompanyCheckBox(
                    checkState = checkState,
                    onCheckChanged = onCheckChanged
                )
            }

            NextButton(
                isKeyboardVisible = isKeyboardVisible,
                isEnabled = uiState.isChecked || uiState.selectedCompany != null,
                padding = innerPadding,
                onClick = onNextBtnClicked
            )
        }
    }
}

@Composable
private fun CompanyHeader() {
    Spacer(modifier = Modifier.height(12.dp))

    DaysStepIndicator(currentStep = 3, totalStep = 5)

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = stringResource(id = R.string.my_profile_company_sub_title),
        style = DaysTheme.typography.regular14.toTextStyle(),
        color = DaysTheme.colors.grey200
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = stringResource(id = R.string.my_profile_company_title),
        style = DaysTheme.typography.semiBold24.toTextStyle(),
        color = DaysTheme.colors.grey500
    )
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

@Preview
@Composable
private fun MyProfileCompanyScreenPreview() {
    MyProfileCompanyScreen(onBackBtnClicked = {}, onNextBtnClicked = {})
}