package com.weave.my_profile.partner.occupation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysJobToggleButton
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.DaysStepIndicator
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.addFocusCleaner
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.my_profile.MyProfileSharedViewModel
import com.weave.utils.Keyboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PartnerOccupationScreen(
    viewModel: PartnerOccupationViewModel = hiltViewModel(),
    sharedViewModel: MyProfileSharedViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val jobToggleItems = remember { toggleItems }
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        if(sharedViewModel.partnerOccupations.isNotEmpty()){
            sharedViewModel.partnerOccupations.forEach {
                viewModel.setAction(
                    OccupationAction.SelectOccupation(it)
                )
            }
        }
    }

    LaunchedEffect(uiState) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is OccupationEffect.NavigateToNextScreen -> {
                    sharedViewModel.partnerOccupations = uiState.selectedOccupations
                    onNextBtnClicked()
                }

                is OccupationEffect.ShowToast -> scope.launch {
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


    PartnerOccupationScreen(
        focusManager = focusManager,
        snackState = snackState,
        jobToggleItems = jobToggleItems,
        uiState = uiState,
        onBackBtnClicked = onBackBtnClicked,
        setAction = {
            viewModel.setAction(it)
        }
    )
}

@Composable
private fun PartnerOccupationScreen(
    snackState: SnackbarHostState,
    focusManager: FocusManager,
    jobToggleItems: List<JobToggleItem>,
    uiState: OccupationState,
    onBackBtnClicked: () -> Unit,
    setAction: (OccupationAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysOnlyBackAppbar(onBackPressed = onBackBtnClicked)
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
                .addFocusCleaner(focusManager)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            DaysBackgroundTextureImage()

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 26.dp)
            ) {
                OccupationHeader()

                Spacer(modifier = Modifier.height(40.dp))

                LazyVerticalGrid(
                    modifier = Modifier.padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding() + 100.dp
                    ),
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    items(jobToggleItems, key = { it.text }) { item ->
                        val isChecked = uiState.selectedOccupations.any { it.koValue == item.text }

                        DaysJobToggleButton(
                            isChecked = isChecked,
                            onToggleChanged = {
                                val occupation = JobOccupation.findFromKoValue(it)

                                occupation?.let { job ->
                                    setAction(OccupationAction.SelectOccupation(job))
                                }
                            },
                            icon = painterResource(id = item.resourceId),
                            text = item.text
                        )
                    }
                }
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = uiState.selectedOccupations.isNotEmpty(),
                padding = innerPadding,
                onClick = {
                    setAction(OccupationAction.ValidateOccupationState)
                }
            )
        }
    }
}

private data class JobToggleItem(
    val text: String,
    val resourceId: Int
)

private val toggleItems = listOf(
    JobToggleItem(JobOccupation.entries[0].koValue, R.drawable.ic_business),
    JobToggleItem(JobOccupation.entries[1].koValue, R.drawable.ic_marketing),
    JobToggleItem(JobOccupation.entries[2].koValue, R.drawable.ic_research),
    JobToggleItem(JobOccupation.entries[3].koValue, R.drawable.ic_tech),
    JobToggleItem(JobOccupation.entries[4].koValue, R.drawable.ic_finance),
    JobToggleItem(JobOccupation.entries[5].koValue, R.drawable.ic_gear),
    JobToggleItem(JobOccupation.entries[6].koValue, R.drawable.ic_education),
    JobToggleItem(JobOccupation.entries[7].koValue, R.drawable.ic_legal),
    JobToggleItem(JobOccupation.entries[8].koValue, R.drawable.ic_security),
    JobToggleItem(JobOccupation.entries[9].koValue, R.drawable.ic_medical),
    JobToggleItem(JobOccupation.entries[10].koValue, R.drawable.ic_media),
    JobToggleItem(JobOccupation.entries[11].koValue, R.drawable.ic_design),
    JobToggleItem(JobOccupation.entries[12].koValue, R.drawable.ic_sports),
    JobToggleItem(JobOccupation.entries[13].koValue, R.drawable.ic_building),
    JobToggleItem(JobOccupation.entries[14].koValue, R.drawable.ic_train),
    JobToggleItem(JobOccupation.entries[15].koValue, R.drawable.ic_leafy),
    JobToggleItem(JobOccupation.entries[16].koValue, R.drawable.ic_speech),
    JobToggleItem(JobOccupation.entries[17].koValue, R.drawable.ic_others)
)

@Composable
private fun OccupationHeader() {
    Spacer(modifier = Modifier.height(12.dp))

    DaysStepIndicator(
        currentStep = 2,
        totalStep = 3,
        pointColor = Color(0xFFFF8BAC)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = stringResource(id = R.string.partner_occupation_sub_title),
        style = DaysTheme.typography.regular14.toTextStyle(),
        color = DaysTheme.colors.grey200
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = stringResource(id = R.string.partner_occupation_title),
        style = DaysTheme.typography.semiBold24.toTextStyle(),
        color = DaysTheme.colors.grey500
    )
}

@Preview
@Composable
private fun PartnerOccupationScreenPreview() {
    val focusManager = LocalFocusManager.current
    val snackState = remember { SnackbarHostState() }
    val jobToggleItems = remember { toggleItems }
    val uiState = OccupationState()

    PartnerOccupationScreen(
        focusManager = focusManager,
        snackState = snackState,
        jobToggleItems = jobToggleItems,
        uiState = uiState,
        onBackBtnClicked = { },
        setAction = {}
    )
}