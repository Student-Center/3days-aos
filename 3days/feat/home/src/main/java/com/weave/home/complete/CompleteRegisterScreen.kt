package com.weave.home.complete

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.domain.user.Gender
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.model.domain.user.UserDesiredPartner
import com.weave.model.domain.user.UserProfile
import com.weave.model.enum.PreferDistance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun CompleteRegisterScreen(
    viewModel: CompleteRegisterViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.setAction(CompleteRegisterAction.FetchMyInfo)
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CompleteRegisterEffect.ShowToast -> scope.launch {
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

    CompleteRegisterScreen(
        myInfo = viewModel.uiState.myInfo,
        snackState = snackState
    )
}

@Composable
private fun CompleteRegisterScreen(
    myInfo: MyInfo? = null,
    snackState: SnackbarHostState
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "ID:\n" + myInfo?.id.toString(),
                    style = DaysTheme.typography.semiBold18.toTextStyle(),
                    color = DaysTheme.colors.grey400
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "이름:\n" + myInfo?.name.toString(),
                    style = DaysTheme.typography.semiBold18.toTextStyle(),
                    color = DaysTheme.colors.grey400
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "휴대폰 번호:\n" + myInfo?.phoneNumber.toString(),
                    style = DaysTheme.typography.semiBold18.toTextStyle(),
                    color = DaysTheme.colors.grey400
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "성별:\n" + myInfo?.profile?.gender.toString(),
                    style = DaysTheme.typography.semiBold18.toTextStyle(),
                    color = DaysTheme.colors.grey400
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "생년:\n" + myInfo?.profile?.birthYear.toString(),
                    style = DaysTheme.typography.semiBold18.toTextStyle(),
                    color = DaysTheme.colors.grey400
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "직무:\n" + myInfo?.profile?.jobOccupation.toString(),
                    style = DaysTheme.typography.semiBold18.toTextStyle(),
                    color = DaysTheme.colors.grey400
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "직무:\n" + myInfo?.profile?.locationIds?.joinToString("\n"),
                    style = DaysTheme.typography.semiBold18.toTextStyle(),
                    color = DaysTheme.colors.grey400
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview
@Composable
private fun CompleteRegisterScreenPreview() {
    val snackState = remember { SnackbarHostState() }

    CompleteRegisterScreen(
        snackState = snackState,
        myInfo = MyInfo(
            id = UUID.randomUUID(),
            name = "테스트",
            phoneNumber = "010-1111-1111",
            profile = UserProfile(
                gender = Gender.MALE,
                birthYear = 2000,
                jobOccupation = JobOccupation.OTHER,
                locationIds = listOf(UUID.randomUUID(), UUID.randomUUID()),
                companyId = UUID.randomUUID()
            ),
            desiredPartner = UserDesiredPartner(
                birthYearRange = BirthYearRange(5, 5),
                jobOccupations = listOf(
                    JobOccupation.OTHER
                ),
                preferDistance = PreferDistance.ANYWHERE
            ),
            profileWidgets = listOf(
                ProfileWidget(ProfileWidgetType.HOBBY, "취미----")
            )
        ),
    )
}