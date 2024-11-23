package com.weave.home.profile.job

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.weave.home.profile.SnackBarViewModel
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.utils.Keyboard

@Composable
fun EditJobScreen(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    viewModel: EditJobViewModel = hiltViewModel(),
    initOccupation: JobOccupation,
    navigateToProfile: (Boolean) -> Unit
) {
    val jobToggleItems = remember { toggleItems }
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.setAction(EditJobAction.FetchData(initOccupation))
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EditJobEffect.NavigateToProfile -> {
                    navigateToProfile(effect.isSuccess)
                }

                is EditJobEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    EditJobScreenContent(
        jobToggleItems = jobToggleItems,
        uiState = uiState,
        snackState = snackBarViewModel.snackBarHostState,
        navigateToProfile = navigateToProfile,
        updateData = {
            viewModel.setAction(EditJobAction.UpdateData(it))
        },
        requestUpdate = {
            viewModel.setAction(EditJobAction.RequestUpdate)
        }
    )
}

@Composable
private fun EditJobScreenContent(
    jobToggleItems: List<JobToggleItem>,
    uiState: EditJobState,
    snackState: SnackbarHostState,
    navigateToProfile: (Boolean) -> Unit,
    updateData: (JobOccupation) -> Unit,
    requestUpdate: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysEditTopBar(
                title = "직군 수정",
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
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(34.dp))

                CurrentItem(
                    item = uiState.initOccupation
                )

                Spacer(modifier = Modifier.height(24.dp))

                GirdJobSection(
                    uiState = uiState,
                    toggleItems = jobToggleItems,
                    updateData = updateData
                )
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = uiState.occupation != uiState.initOccupation,
                padding = innerPadding,
                onClick = requestUpdate
            )
        }
    }
}

@Composable
private fun CurrentItem(
    item: JobOccupation
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
                painter = painterResource(id = com.weave.design_system.R.drawable.ic_business),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "내 직군",
                style = DaysTheme.typography.regular12.toTextStyle(),
                color = DaysTheme.colors.grey400
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.koValue,
                style = DaysTheme.typography.semiBold14.copy(fontSize = 12.dp).toTextStyle(),
                color = DaysTheme.colors.grey400
            )
        }
    }
}

@Composable
private fun GirdJobSection(
    uiState: EditJobState,
    toggleItems: List<JobToggleItem>,
    updateData: (JobOccupation) -> Unit
) {
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
        items(toggleItems, key = { it.text }) { item ->
            DaysJobToggleButton(
                isChecked = uiState.occupation?.koValue == item.text,
                onToggleChanged = {
                    val occupation =
                        if (uiState.occupation?.koValue == it) {
                            null
                        } else {
                            JobOccupation.findFromKoValue(it)
                        }

                    occupation?.let { data -> updateData(data) }
                },
                icon = painterResource(id = item.resourceId),
                text = item.text
            )
        }
    }
}

@Preview
@Composable
private fun EditJobScreenPreview() {
    EditJobScreenContent(
        jobToggleItems = toggleItems,
        uiState = EditJobState(),
        snackState = remember {
            SnackbarHostState()
        },
        navigateToProfile = {},
        updateData = {},
        requestUpdate = {}
    )
}