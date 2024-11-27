package com.weave.home.profile.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysEditTopBar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.profile.SnackBarViewModel
import com.weave.home.profile.UserInfo
import com.weave.model.domain.myprofile.Location
import com.weave.utils.Keyboard

@Composable
fun EditLocationScreen(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    viewModel: EditLocationViewModel = hiltViewModel(),
    userInfo: UserInfo,
    navigateToProfile: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.setAction(EditLocationAction.FetchData(userInfo))
        viewModel.setAction(EditLocationAction.GetRegions)
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EditLocationEffect.NavigateToProfile -> {
                    navigateToProfile(effect.isSuccess)
                }

                is EditLocationEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    EditLocationScreenContent(
        uiState = viewModel.uiState,
        snackState = snackBarViewModel.snackBarHostState,
        navigateToProfile = navigateToProfile,
        selectedLocation = {
            viewModel.setAction(EditLocationAction.SelectLocation(it))
        },
        selectedRegionName = {
            viewModel.setAction(EditLocationAction.SelectRegionName(it))
        },
        requestUpdate = {
            viewModel.setAction(EditLocationAction.ValidateInput)
        }
    )
}

@Composable
private fun EditLocationScreenContent(
    uiState: EditLocationState,
    snackState: SnackbarHostState,
    navigateToProfile: (Boolean) -> Unit,
    selectedLocation: (Location) -> Unit,
    selectedRegionName: (String) -> Unit,
    requestUpdate: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysEditTopBar(
                title = "활동 지역 수정",
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
                Spacer(modifier = Modifier.height(34.dp))

                CurrentItem(
                    item = uiState.initLocations.map { it.second }
                )

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    modifier = Modifier.height(34.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "내 지역",
                        style = DaysTheme.typography.medium14.toTextStyle(),
                        color = DaysTheme.colors.grey300
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    LazyRow(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(uiState.selectedLocations, key = { it.id }) {
                            LocationChip(
                                location = it,
                                showCancelButton = true,
                                isChecked = true,
                                onClick = { selectedLocation(it) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LocationBoard(
                    regionNames = uiState.locations.map { it.regionName },
                    locations = uiState.locations.find { it.regionName == uiState.selectedRegionName }?.locations
                        ?: listOf(),
                    selectedRegionName = uiState.selectedRegionName,
                    selectedLocations = uiState.selectedLocations,
                    onClickLocation = selectedLocation,
                    onClickRegionName = selectedRegionName,
                )
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = uiState.selectedLocations.isNotEmpty(),
                padding = innerPadding,
                onClick = requestUpdate
            )
        }
    }
}

@Composable
private fun CurrentItem(
    item: List<String>
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
                painter = painterResource(id = com.weave.home.R.drawable.ic_compass),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "내 활동 지역",
                style = DaysTheme.typography.regular12.toTextStyle(),
                color = DaysTheme.colors.grey400
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.joinToString(),
                style = DaysTheme.typography.semiBold14.copy(fontSize = 12.dp).toTextStyle(),
                color = DaysTheme.colors.grey400
            )
        }
    }
}

@Composable
private fun LocationChip(
    location: Location,
    showCancelButton: Boolean,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    val checkedColors = listOf(Color(0xFF93CAF8), Color(0xFF76B6EB))
    val chipColor =
        if (isChecked) checkedColors else listOf(DaysTheme.colors.white, DaysTheme.colors.white)
    val textColor = if (isChecked) DaysTheme.colors.white else DaysTheme.colors.blue500
    val borderColor = if (isChecked) DaysTheme.colors.white else Color(0xFFDFE8EF)

    Box(
        modifier = Modifier
            .height(34.dp)
            .background(
                shape = RoundedCornerShape(52.dp),
                brush = Brush.verticalGradient(chipColor)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(52.dp)
            )
            .padding(horizontal = 12.dp)
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = location.subRegion,
                style = DaysTheme.typography.medium14.toTextStyle(),
                color = textColor
            )

            if (showCancelButton) {
                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.size(size = 16.dp),
                    imageVector = Icons.Default.Clear,
                    tint = DaysTheme.colors.white,
                    contentDescription = "Chip Clear"
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LocationBoard(
    regionNames: List<String>,
    locations: List<Location>,
    selectedLocations: List<Location>,
    selectedRegionName: String,
    onClickRegionName: (String) -> Unit,
    onClickLocation: (Location) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + 100.dp
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        LazyColumn(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(regionNames, key = { it }) {
                val regionBarColor =
                    if (selectedRegionName == it) DaysTheme.colors.blue500 else Color(0xFFD3D3D3)

                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 42.dp)
                        .background(
                            color = regionBarColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .applyShadow(shape = RoundedCornerShape(12.dp))
                        .noRippleClickable { onClickRegionName(it) },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 14.dp),
                        text = it,
                        style = DaysTheme.typography.semiBold14.toTextStyle(),
                        color = DaysTheme.colors.white
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 55.dp)
                .background(
                    color = DaysTheme.colors.blue50,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 4.dp,
                    color = DaysTheme.colors.white,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(start = 20.dp, end = 20.dp, top = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(height = 20.dp, width = 20.dp),
                    imageVector = Icons.Default.LocationOn,
                    tint = DaysTheme.colors.blue500,
                    contentDescription = "Region Name"
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = selectedRegionName,
                    style = DaysTheme.typography.medium14.toTextStyle(),
                    color = DaysTheme.colors.blue500
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ContextualFlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                maxItemsInEachRow = 5,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                itemCount = locations.size
            ) { index ->
                if (locations.isNotEmpty() && locations.size > index) {
                    val location = locations[index]

                    LocationChip(
                        location = location,
                        showCancelButton = false,
                        isChecked = selectedLocations.contains(location),
                        onClick = { onClickLocation(location) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditLocationScreenPreview() {
    EditLocationScreenContent(
        uiState = EditLocationState(),
        snackState = SnackbarHostState(),
        navigateToProfile = {},
        selectedLocation = {},
        selectedRegionName = {},
        requestUpdate = {}
    )
}