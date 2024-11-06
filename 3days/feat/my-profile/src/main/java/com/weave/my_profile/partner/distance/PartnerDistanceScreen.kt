package com.weave.my_profile.partner.distance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysOnlyBackAppbar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.DaysStepIndicator
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.model.domain.myprofile.Location
import com.weave.model.enum.PreferDistance
import com.weave.my_profile.MyProfileSharedViewModel
import com.weave.design_system.R as design
import com.weave.utils.Keyboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


@Composable
fun PartnerDistanceScreen(
    viewModel: PartnerDistanceViewModel = hiltViewModel(),
    sharedViewModel: MyProfileSharedViewModel = hiltViewModel(),
    onBackBtnClicked: () -> Unit,
    onNextBtnClicked: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        sharedViewModel.distance?.let {
            viewModel.setAction(PartnerDistanceAction.SetDistance(it))
        }
    }

    LaunchedEffect(uiState) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PartnerDistanceEffect.NavigateToNextScreen -> {
                    sharedViewModel.distance = uiState.distance
                    onNextBtnClicked()
                }

                is PartnerDistanceEffect.ShowToast -> scope.launch {
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

    PartnerDistanceScreen(
        snackState = snackState,
        uiState = uiState,
        locations = sharedViewModel.locations,
        onBackBtnClicked = onBackBtnClicked,
        setAction = { viewModel.setAction(it) }
    )
}

@Composable
private fun PartnerDistanceScreen(
    snackState: SnackbarHostState,
    uiState: PartnerDistanceState,
    locations: List<Location>,
    onBackBtnClicked: () -> Unit,
    setAction: (PartnerDistanceAction) -> Unit
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
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            DaysBackgroundTextureImage()

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 26.dp)
            ) {
                PartnerDistanceHeader()

                Spacer(modifier = Modifier.height(40.dp))

                MyLocations(
                    locations = locations
                )

                Spacer(modifier = Modifier.height(22.dp))

                DistanceOptions(
                    selectedOption = uiState.distance,
                    onOptionChange = { setAction(PartnerDistanceAction.SetDistance(it)) }
                )
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = uiState.distance != null,
                padding = innerPadding,
                onClick = {
                    setAction(PartnerDistanceAction.ValidateInput)
                }
            )
        }
    }
}

@Composable
private fun PartnerDistanceHeader() {
    Spacer(modifier = Modifier.height(12.dp))

    DaysStepIndicator(
        currentStep = 3,
        totalStep = 3,
        pointColor = Color(0xFFFF8BAC)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = stringResource(id = design.string.partner_distance_sub_title),
        style = DaysTheme.typography.regular14.toTextStyle(),
        color = DaysTheme.colors.grey200
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = stringResource(id = design.string.partner_distance_title),
        style = DaysTheme.typography.semiBold24.toTextStyle(),
        color = DaysTheme.colors.grey500
    )
}

@Composable
private fun MyLocations(
    locations: List<Location>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp),
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
                    color = Color(0xFFEDE9C1)
                )
                .padding(horizontal = 19.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = com.weave.design_system.R.drawable.ic_others),
                contentDescription = ""
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = "내 활동 지역",
                style = DaysTheme.typography.regular12.toTextStyle(),
                color = DaysTheme.colors.grey400
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = locations.joinToString { it.subRegion },
                style = DaysTheme.typography.semiBold14.copy(fontSize = 12.dp).toTextStyle(),
                color = DaysTheme.colors.grey400
            )
        }
    }
}

@Composable
private fun DistanceOptions(
    selectedOption: PreferDistance?,
    onOptionChange: (PreferDistance) -> Unit
) {

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier) {
            DistanceOption(
                option = PreferDistance.ONLY_MY_AREA,
                selectedOption = selectedOption,
                onOptionChange = onOptionChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            DistanceOption(
                option = PreferDistance.INCLUDE_SURROUNDING_REGIONS,
                selectedOption = selectedOption,
                onOptionChange = onOptionChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            DistanceOption(
                option = PreferDistance.ANYWHERE,
                selectedOption = selectedOption,
                onOptionChange = onOptionChange
            )
        }
    }

}

@Composable
private fun DistanceOption(
    option: PreferDistance,
    selectedOption: PreferDistance?,
    onOptionChange: (PreferDistance) -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    val containerColor =
        if (option == selectedOption) listOf(Color(0xFF93CAF8), Color(0xFF76B6EB)) else listOf(
            DaysTheme.colors.blue50,
            DaysTheme.colors.blue50
        )
    val textColor =
        if (option == selectedOption) DaysTheme.colors.white else DaysTheme.colors.grey500

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .applyShadow(
                shape = shape
            )
            .background(
                brush = Brush.horizontalGradient(containerColor),
                shape = shape
            )
            .border(
                width = 3.dp,
                color = DaysTheme.colors.white,
                shape = shape
            )
            .clip(shape)
            .noRippleClickable { onOptionChange(option) }
            .padding(horizontal = 55.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option.koValue,
            style = DaysTheme.typography.medium14.toTextStyle(),
            color = textColor
        )
    }
}

@Preview
@Composable
private fun PartnerDistanceScreenPreview() {
    val snackState = remember { SnackbarHostState() }

    PartnerDistanceScreen(
        snackState = snackState,
        uiState = PartnerDistanceState(),
        locations = listOf(
            Location(UUID.randomUUID(), "서브", "용인"),
            Location(UUID.randomUUID(), "서브", "강남구"),
            Location(UUID.randomUUID(), "서브", "중구"),
            Location(UUID.randomUUID(), "서브", "강남구"),
            Location(UUID.randomUUID(), "서브", "강남구"),
        ),
        onBackBtnClicked = { },
        setAction = { }
    )
}