package com.weave.my_profile.location

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.DaysStepIndicator
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.addFocusCleaner
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.model.domain.myprofile.Location
import com.weave.my_profile.MyProfileSharedViewModel
import com.weave.utils.Keyboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun MyProfileLocationScreen(
    viewModel: MyProfileLocationViewModel = hiltViewModel(),
    sharedViewModel: MyProfileSharedViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setAction(LocationAction.RestoreSelectedLocations(sharedViewModel.locations))

        viewModel.setAction(LocationAction.GetRegions)
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LocationEffect.NavigateToNextScreen -> {
                    sharedViewModel.locations = viewModel.uiState.selectedLocations
                    onNextBtnClicked()
                }

                is LocationEffect.ShowToast -> scope.launch {
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
                LocationHeader()

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.height(34.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "내 지역",
                        style = DaysTheme.typography.medium14.toTextStyle(),
                        color = DaysTheme.colors.grey300
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    LazyRow(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(viewModel.uiState.selectedLocations, key = { it.id }) {
                            LocationChip(
                                location = it,
                                showCancelButton = true,
                                isChecked = true,
                                onClick = {
                                    viewModel.setAction(LocationAction.SelectLocation(it))
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LocationBoard(
                    regionNames = viewModel.uiState.locations.map { it.regionName },
                    locations = viewModel.uiState.locations.find { it.regionName == viewModel.uiState.selectedRegionName }?.locations
                        ?: listOf(),
                    selectedRegionName = viewModel.uiState.selectedRegionName,
                    selectedLocations = viewModel.uiState.selectedLocations,
                    onClickLocation = { viewModel.setAction(LocationAction.SelectLocation(it)) },
                    onClickRegionName = {
                        viewModel.setAction(LocationAction.SelectRegionName(it))
                    },
                )
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = viewModel.uiState.selectedLocations.isNotEmpty(),
                padding = innerPadding,
                onClick = {
                    viewModel.setAction(LocationAction.ValidateInput)
                }
            )
        }
    }
}

@Composable
private fun LocationHeader() {
    Spacer(modifier = Modifier.height(12.dp))

    DaysStepIndicator(currentStep = 5, totalStep = 5)

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = stringResource(id = R.string.my_profile_location_sub_title),
        style = DaysTheme.typography.regular14.toTextStyle(),
        color = DaysTheme.colors.grey200
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = stringResource(id = R.string.my_profile_location_title),
        style = DaysTheme.typography.semiBold24.toTextStyle(),
        color = DaysTheme.colors.grey500
    )
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
private fun LocationBoardPreview() {
    var selectedRegionName by remember {
        mutableStateOf("서울")
    }
    val regions = listOf(
        "강원",
        "경기",
        "경남",
        "경북",
        "광주",
        "대구",
        "대전",
        "부산",
        "서울",
        "세종",
        "울산",
        "인천",
        "전남",
        "전북",
        "제주",
        "충남",
        "충북"
    )

    val locations = regions.map {
        MyProfileLocation(
            it, listOf(
                Location(
                    id = UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c76"),
                    region = "서울",
                    subRegion = "동대문구"
                ),
                Location(
                    id = UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c77"),
                    region = "서울",
                    subRegion = "중랑구"
                ),
                Location(
                    id = UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c78"),
                    region = "서울",
                    subRegion = "성북구"
                ),
                Location(
                    id = UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c70"),
                    region = "서울",
                    subRegion = "동대문구"
                ),
                Location(
                    id = UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c71"),
                    region = "서울",
                    subRegion = "중랑구"
                ),
                Location(
                    id = UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c72"),
                    region = "서울",
                    subRegion = "성북구"
                ),
            )
        )
    }

    LocationBoard(
        regionNames = locations.map { it.regionName },
        locations = locations.find { it.regionName == selectedRegionName }?.locations ?: listOf(),
        selectedLocations = listOf(),
        selectedRegionName = selectedRegionName,
        onClickRegionName = {
            selectedRegionName = it
        },
        onClickLocation = {},
    )
}

@Preview
@Composable
private fun LocationChipPreview() {
    LocationChip(
        location = Location(UUID.randomUUID(), "123", "강남구"),
        showCancelButton = true,
        isChecked = true,
        onClick = {}
    )
}

@Preview
@Composable
private fun MyProfileLocationScreenPreview() {
    MyProfileLocationScreen(
        onBackBtnClicked = {},
        onNextBtnClicked = {}
    )
}