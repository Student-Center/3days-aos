package com.weave.home.profile.main.date.job

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysEditTopBar
import com.weave.design_system.component.DaysJobToggleButton
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.NextButton
import com.weave.home.profile.UserInfo
import com.weave.home.profile.job.JobToggleItem
import com.weave.home.profile.job.toggleItems
import com.weave.home.profile.main.SnackBarViewModel
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.utils.Keyboard

@Composable
fun EditPartnerJobScreen(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    viewModel: EditPartnerJobViewModel = hiltViewModel(),
    userInfo: UserInfo,
    navigateToProfile: (Boolean) -> Unit
) {
    val jobToggleItems = remember { toggleItems }

    LaunchedEffect(Unit) {
        viewModel.setAction(EditPartnerJobAction.FetchData(userInfo))
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EditPartnerJobEffect.NavigateToProfile -> {
                    navigateToProfile(true)
                }

                is EditPartnerJobEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    PartnerOccupationScreen(
        snackState = snackBarViewModel.snackBarHostState,
        jobToggleItems = jobToggleItems,
        uiState = viewModel.uiState,
        navigateToProfile = navigateToProfile,
        setAction = { viewModel.setAction(it) }
    )
}

@Composable
private fun PartnerOccupationScreen(
    snackState: SnackbarHostState,
    jobToggleItems: List<JobToggleItem>,
    uiState: EditPartnerJobState,
    navigateToProfile: (Boolean) -> Unit,
    setAction: (EditPartnerJobAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysEditTopBar(
                title = "선호 직군 수정",
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
                    .padding(horizontal = 26.dp)
            ) {

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier.padding(
                            top = 20.dp,
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding() + 90.dp
                        ),
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        repeat(3) {
                            item { Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)) }
                        }

                        itemsIndexed(
                            jobToggleItems,
                            key = { _, item -> item.text }) { index, item ->
                            val isChecked =
                                uiState.selectedOccupations.any { it.koValue == item.text }

                            DaysJobToggleButton(
                                isChecked = isChecked,
                                onToggleChanged = {
                                    val occupation = JobOccupation.findFromKoValue(it)

                                    occupation?.let { job ->
                                        setAction(EditPartnerJobAction.SelectOccupation(job))
                                    }
                                },
                                icon = painterResource(id = item.resourceId),
                                text = item.text
                            )
                        }

                        if (jobToggleItems.size % 3 == 0) {
                            item { Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)) }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        DaysTheme.colors.bgDefault,
                                        DaysTheme.colors.bgDefault.copy(alpha = 0.5f),
                                        DaysTheme.colors.bgDefault.copy(alpha = 0.1f)
                                    )
                                )
                            )
                    )
                }
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = uiState.selectedOccupations.isNotEmpty(),
                padding = innerPadding,
                onClick = {
                    setAction(EditPartnerJobAction.UpdateData)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PartnerOccupationScreenPreview() {
    val snackState = remember { SnackbarHostState() }
    val jobToggleItems = remember { toggleItems }
    val uiState = EditPartnerJobState()

    PartnerOccupationScreen(
        snackState = snackState,
        jobToggleItems = jobToggleItems,
        uiState = uiState,
        navigateToProfile = { },
        setAction = {}
    )
}