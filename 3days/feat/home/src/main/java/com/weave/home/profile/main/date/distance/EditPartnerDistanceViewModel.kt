package com.weave.home.profile.main.date.distance

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.home.profile.UserInfo
import com.weave.home.profile.main.date.job.EditPartnerJobAction
import com.weave.home.profile.main.date.job.EditPartnerJobEffect
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.enum.PreferDistance
import com.weave.user.RegisterUserUseCase
import com.weave.user.UpdateUserDesiredPartnerUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditPartnerDistanceAction : UIAction {
    data class FetchData(val userInfo: UserInfo) : EditPartnerDistanceAction()
    data class SetDistance(val distance: PreferDistance) : EditPartnerDistanceAction()
    data object UpdateData : EditPartnerDistanceAction()
}

sealed class EditPartnerDistanceIntent : UIIntent {
    data class FetchData(val userInfo: UserInfo) : EditPartnerDistanceIntent()
    data class SetDistance(val distance: PreferDistance) : EditPartnerDistanceIntent()
    data object UpdateData : EditPartnerDistanceIntent()
}

data class EditPartnerDistanceState(
    val userInfo: UserInfo? = null,
    val initDistance: PreferDistance? = null,
    val distance: PreferDistance? = null
) : UIState

sealed class EditPartnerDistanceEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : EditPartnerDistanceEffect()
    data object NavigateToProfile : EditPartnerDistanceEffect()

}

@HiltViewModel
class EditPartnerDistanceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val updatePartnerPreferDistanceUseCase: UpdateUserDesiredPartnerUseCase
) : BaseViewModel<EditPartnerDistanceAction, EditPartnerDistanceIntent, EditPartnerDistanceState, EditPartnerDistanceEffect>(
    initialState = EditPartnerDistanceState()
) {

    override fun actionPredicate(action: EditPartnerDistanceAction): EditPartnerDistanceIntent {
        return when (action) {
            is EditPartnerDistanceAction.FetchData -> EditPartnerDistanceIntent.FetchData(action.userInfo)
            is EditPartnerDistanceAction.SetDistance -> EditPartnerDistanceIntent.SetDistance(action.distance)
            is EditPartnerDistanceAction.UpdateData -> EditPartnerDistanceIntent.UpdateData
        }
    }

    override fun collectIntent(intent: EditPartnerDistanceIntent) {
        when (intent) {
            is EditPartnerDistanceIntent.FetchData -> fetchData(intent.userInfo)
            is EditPartnerDistanceIntent.SetDistance -> setDistance(intent.distance)
            is EditPartnerDistanceIntent.UpdateData -> updateData()
        }
    }

    private fun fetchData(userInfo: UserInfo?) = setState {
        copy(
            userInfo = userInfo,
            initDistance = userInfo?.desiredPartner?.preferDistance ?: PreferDistance.ANYWHERE,
            distance = userInfo?.desiredPartner?.preferDistance ?: PreferDistance.ANYWHERE
        )
    }

    private fun setDistance(distance: PreferDistance) {
        val value = if (uiState.distance == distance) null else distance
        setState { copy(distance = value) }
    }

    private fun updateData() {
        if (uiState.distance != null) {
            if (uiState.initDistance != uiState.distance) {
                viewModelScope.launch {
                    updatePartnerPreferDistanceUseCase.invoke(
                        birthYearRange = uiState.userInfo?.desiredPartner?.birthYearRange
                            ?: BirthYearRange(),
                        jobOccupations = uiState.userInfo?.desiredPartner?.jobOccupations
                            ?: listOf(),
                        preferDistance = uiState.distance ?: PreferDistance.ANYWHERE
                    ).mapMerge().collect { result ->
                        if (result != null) {
                            setEffect {
                                EditPartnerDistanceEffect.ShowToast(
                                    message = "나의 선호 거리가 변경되었어요",
                                    type = SnackBarType.DEFAULT
                                )
                            }
                            setEffect { EditPartnerDistanceEffect.NavigateToProfile }
                        } else if (!isLoading) {
                            setEffect {
                                EditPartnerDistanceEffect.ShowToast(
                                    message = "다시 시도해 주세요",
                                    type = SnackBarType.ERROR
                                )
                            }
                        }
                    }
                }
            }
        } else {
            setEffect {
                EditPartnerDistanceEffect.ShowToast(
                    message = context.getString(com.weave.design_system.R.string.partner_distance_not_selected_error_message),
                    type = SnackBarType.ERROR
                )
            }
        }
    }
}