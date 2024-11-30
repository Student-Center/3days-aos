package com.weave.home.profile.main.date.job

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.home.profile.UserInfo
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.enum.PreferDistance
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

sealed class EditPartnerJobAction : UIAction {
    data class FetchData(val userInfo: UserInfo) : EditPartnerJobAction()
    data class SelectOccupation(val occupation: JobOccupation) : EditPartnerJobAction()
    data object UpdateData : EditPartnerJobAction()
}

sealed class EditPartnerJobIntent : UIIntent {
    data class FetchData(val userInfo: UserInfo) : EditPartnerJobIntent()

    data class SelectOccupation(val occupation: JobOccupation) : EditPartnerJobIntent()
    data object UpdateData : EditPartnerJobIntent()
}

data class EditPartnerJobState(
    val errorMessage: String = "",
    val userInfo: UserInfo? = null,
    val initJobOccupations: List<JobOccupation> = listOf(),
    val selectedOccupations: List<JobOccupation> = listOf(),
) : UIState

sealed class EditPartnerJobEffect : UIEffect {
    data object NavigateToProfile : EditPartnerJobEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : EditPartnerJobEffect()
}

@HiltViewModel
class EditPartnerJobViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val updatePartnerJobUseCase: UpdateUserDesiredPartnerUseCase
) : BaseViewModel<EditPartnerJobAction, EditPartnerJobIntent, EditPartnerJobState, EditPartnerJobEffect>(
    initialState = EditPartnerJobState()
) {
    override fun actionPredicate(action: EditPartnerJobAction): EditPartnerJobIntent {
        return when (action) {
            is EditPartnerJobAction.FetchData -> EditPartnerJobIntent.FetchData(action.userInfo)
            is EditPartnerJobAction.SelectOccupation -> EditPartnerJobIntent.SelectOccupation(action.occupation)
            is EditPartnerJobAction.UpdateData -> EditPartnerJobIntent.UpdateData
        }
    }

    override fun collectIntent(intent: EditPartnerJobIntent) {
        when (intent) {
            is EditPartnerJobIntent.FetchData -> fetchData(intent.userInfo)
            is EditPartnerJobIntent.SelectOccupation -> selectOccupation(intent.occupation)
            is EditPartnerJobIntent.UpdateData -> updateData()
        }
    }

    private fun fetchData(userInfo: UserInfo) =
        setState {
            copy(
                userInfo = userInfo,
                initJobOccupations = userInfo.desiredPartner?.jobOccupations ?: listOf(),
                selectedOccupations = userInfo.desiredPartner?.jobOccupations ?: listOf()
            )
        }

    private fun selectOccupation(occupation: JobOccupation) {
        val newList = uiState.selectedOccupations.toMutableList()

        if (uiState.selectedOccupations.contains(occupation)) {
            newList.remove(occupation)
        } else {
            newList.add(occupation)
        }

        setState { copy(selectedOccupations = newList) }
    }

    private fun updateData() {
        if (uiState.selectedOccupations.isNotEmpty()) {
            if (uiState.selectedOccupations != uiState.initJobOccupations) {
                viewModelScope.launch {
                    updatePartnerJobUseCase.invoke(
                        birthYearRange = uiState.userInfo?.desiredPartner?.birthYearRange
                            ?: BirthYearRange(),
                        jobOccupations = uiState.selectedOccupations,
                        preferDistance = uiState.userInfo?.desiredPartner?.preferDistance
                            ?: PreferDistance.ANYWHERE
                    ).mapMerge().collect { result ->
                        if (result != null) {
                            setEffect {
                                EditPartnerJobEffect.ShowToast(
                                    message = "나의 선호 직군이 변경되었어요",
                                    type = SnackBarType.DEFAULT
                                )
                            }
                            setEffect { EditPartnerJobEffect.NavigateToProfile }
                        } else if (!isLoading) {
                            setEffect {
                                EditPartnerJobEffect.ShowToast(
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
                EditPartnerJobEffect.ShowToast(
                    message = context.getString(R.string.partner_occupation_not_selected_error_message),
                    type = SnackBarType.ERROR
                )
            }
        }
    }
}