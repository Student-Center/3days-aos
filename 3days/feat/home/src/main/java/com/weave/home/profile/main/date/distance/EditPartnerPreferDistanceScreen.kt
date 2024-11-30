package com.weave.home.profile.main.date.distance

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysEditTopBar
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.component.NextButton
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.profile.UserInfo
import com.weave.home.profile.main.SnackBarViewModel
import com.weave.model.domain.myprofile.Location
import com.weave.model.enum.PreferDistance
import com.weave.utils.Keyboard

@Composable
fun EditPartnerPreferDistanceScreen(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    viewModel: EditPartnerDistanceViewModel = hiltViewModel(),
    userInfo: UserInfo,
    navigateToProfile: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.setAction(EditPartnerDistanceAction.FetchData(userInfo))
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EditPartnerDistanceEffect.NavigateToProfile -> {
                    navigateToProfile(true)
                }

                is EditPartnerDistanceEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    PartnerDistanceScreen(
        snackState = snackBarViewModel.snackBarHostState,
        uiState = viewModel.uiState,
        navigateToProfile = navigateToProfile,
        setAction = { viewModel.setAction(it) }
    )
}

@Composable
private fun PartnerDistanceScreen(
    snackState: SnackbarHostState,
    uiState: EditPartnerDistanceState,
    navigateToProfile: (Boolean) -> Unit,
    setAction: (EditPartnerDistanceAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysEditTopBar(
                title = "선호 거리 수정",
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

                Spacer(modifier = Modifier.height(40.dp))

                MyLocations(
                    locations = uiState.userInfo?.locations?.map {
                        Location(
                            it.first,
                            "",
                            it.second
                        )
                    } ?: listOf()
                )

                Spacer(modifier = Modifier.height(22.dp))

                DistanceOptions(
                    selectedOption = uiState.distance,
                    onOptionChange = { setAction(EditPartnerDistanceAction.SetDistance(it)) }
                )
            }

            NextButton(
                isKeyboardVisible = Keyboard.Closed,
                isEnabled = uiState.distance != null && uiState.initDistance != uiState.distance,
                padding = innerPadding,
                onClick = {
                    setAction(EditPartnerDistanceAction.UpdateData)
                }
            )
        }
    }
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
                painter = painterResource(id = R.drawable.ic_others),
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
private fun EditPartnerDistanceScreenPreview() {
    val snackState = remember { SnackbarHostState() }

    PartnerDistanceScreen(
        snackState = snackState,
        uiState = EditPartnerDistanceState(),
        navigateToProfile = { },
        setAction = { }
    )
}