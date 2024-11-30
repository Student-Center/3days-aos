package com.weave.home.profile.main.date.age

import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
import com.weave.home.profile.UserInfo
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.enum.PreferDistance
import com.weave.user.UpdateUserDesiredPartnerUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditPartnerAgeAction : UIAction {
    data class FetchData(val userInfo: UserInfo) : EditPartnerAgeAction()
    data object UpdateData : EditPartnerAgeAction()
    data class SetUnderAge(val age: String) : EditPartnerAgeAction()
    data class SetUpperAge(val age: String) : EditPartnerAgeAction()
}

sealed class EditPartnerAgeIntent : UIIntent {
    data object UpdateData : EditPartnerAgeIntent()
    data class FetchData(val userInfo: UserInfo) : EditPartnerAgeIntent()
    data class SetUnderAge(val age: String) : EditPartnerAgeIntent()
    data class SetUpperAge(val age: String) : EditPartnerAgeIntent()
}

data class EditPartnerAgeState(
    val userInfo: UserInfo? = null,
    val initAgeRange: BirthYearRange = BirthYearRange(),
    val ageRange: BirthYearRange = BirthYearRange(),
    val errorMessage: String = "",
) : UIState

sealed class EditPartnerAgeEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : EditPartnerAgeEffect()
    data object NavigateToProfile : EditPartnerAgeEffect()

}

@HiltViewModel
class EditPartnerAgeViewModel @Inject constructor(
    private val updatePartnerAgeUseCase: UpdateUserDesiredPartnerUseCase
) : BaseViewModel<EditPartnerAgeAction, EditPartnerAgeIntent, EditPartnerAgeState, EditPartnerAgeEffect>(
    initialState = EditPartnerAgeState()
) {

    override fun actionPredicate(action: EditPartnerAgeAction): EditPartnerAgeIntent {
        return when (action) {
            is EditPartnerAgeAction.UpdateData -> EditPartnerAgeIntent.UpdateData
            is EditPartnerAgeAction.FetchData -> EditPartnerAgeIntent.FetchData(action.userInfo)
            is EditPartnerAgeAction.SetUpperAge -> EditPartnerAgeIntent.SetUpperAge(action.age)
            is EditPartnerAgeAction.SetUnderAge -> EditPartnerAgeIntent.SetUnderAge(action.age)
        }
    }

    override fun collectIntent(intent: EditPartnerAgeIntent) {
        when (intent) {
            is EditPartnerAgeIntent.UpdateData -> updateData()
            is EditPartnerAgeIntent.FetchData -> fetchData(intent.userInfo)
            is EditPartnerAgeIntent.SetUpperAge -> setUpperAge(intent.age)
            is EditPartnerAgeIntent.SetUnderAge -> setUnderAge(intent.age)
        }
    }

    private fun fetchData(userInfo: UserInfo) {
        setState {
            copy(
                userInfo = userInfo,
                initAgeRange = userInfo.desiredPartner?.birthYearRange ?: BirthYearRange(),
                ageRange = userInfo.desiredPartner?.birthYearRange ?: BirthYearRange()
            )
        }
    }

    private fun setUnderAge(age: String) =
        setState { copy(ageRange = uiState.initAgeRange.copy(start = age.toIntOrNull())) }

    private fun setUpperAge(age: String) =
        setState { copy(ageRange = uiState.initAgeRange.copy(end = age.toIntOrNull())) }

    private fun updateData() {
        if (uiState.initAgeRange == uiState.ageRange) {
            setEffect {
                EditPartnerAgeEffect.ShowToast(
                    message = "선호 연령을 변경해 주세요",
                    type = SnackBarType.ERROR
                )
            }
        } else {
            viewModelScope.launch {
                updatePartnerAgeUseCase.invoke(
                    birthYearRange = uiState.ageRange,
                    jobOccupations = uiState.userInfo?.desiredPartner?.jobOccupations ?: listOf(),
                    preferDistance = uiState.userInfo?.desiredPartner?.preferDistance
                        ?: PreferDistance.ANYWHERE
                ).mapMerge().collect { result ->
                    if (result != null) {
                        setEffect {
                            EditPartnerAgeEffect.ShowToast(
                                message = "나의 선호 연령이 변경되었어요",
                                type = SnackBarType.DEFAULT
                            )
                        }
                        setEffect { EditPartnerAgeEffect.NavigateToProfile }
                    } else if (!isLoading) {
                        setEffect {
                            EditPartnerAgeEffect.ShowToast(
                                message = "다시 시도해 주세요",
                                type = SnackBarType.ERROR
                            )
                        }
                    }
                }
            }
        }
    }
}