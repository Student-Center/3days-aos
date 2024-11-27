package com.weave.home.profile.job

import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
import com.weave.home.profile.UserInfo
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.user.UpdateMyInfoUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditJobAction : UIAction {
    data class FetchData(val userInfo: UserInfo) : EditJobAction()
    data class UpdateData(val occupation: JobOccupation) : EditJobAction()
    data object RequestUpdate : EditJobAction()
}

sealed class EditJobIntent : UIIntent {
    data class FetchData(val userInfo: UserInfo) : EditJobIntent()
    data class UpdateData(val occupation: JobOccupation) : EditJobIntent()
    data object RequestUpdate : EditJobIntent()
}

data class EditJobState(
    val userInfo: UserInfo? = null,
    val occupation: JobOccupation? = null,
    val initOccupation: JobOccupation = JobOccupation.OTHER
) : UIState

sealed class EditJobEffect : UIEffect {
    data class NavigateToProfile(val isSuccess: Boolean) : EditJobEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : EditJobEffect()
}

@HiltViewModel
class EditJobViewModel @Inject constructor(
    private val updateMyInfoUseCase: UpdateMyInfoUseCase
) : BaseViewModel<EditJobAction, EditJobIntent, EditJobState, EditJobEffect>(initialState = EditJobState()) {

    override fun actionPredicate(action: EditJobAction): EditJobIntent {
        return when (action) {
            is EditJobAction.FetchData -> EditJobIntent.FetchData(action.userInfo)
            is EditJobAction.UpdateData -> EditJobIntent.UpdateData(action.occupation)
            is EditJobAction.RequestUpdate -> EditJobIntent.RequestUpdate
        }
    }

    override fun collectIntent(intent: EditJobIntent) {
        when (intent) {
            is EditJobIntent.FetchData -> fetchData(intent.userInfo)
            is EditJobIntent.UpdateData -> updateData(intent.occupation)
            is EditJobIntent.RequestUpdate -> requestUpdate()
        }
    }

    private fun fetchData(data: UserInfo) = setState {
        copy(
            userInfo = data,
            initOccupation = data.jobOccupation,
            occupation = data.jobOccupation
        )
    }

    private fun updateData(data: JobOccupation) = setState { copy(occupation = data) }

    private fun requestUpdate() {
        if (uiState.occupation == uiState.initOccupation) {
            setEffect {
                EditJobEffect.ShowToast(
                    message = "변경할 직군을 선택해 주세요",
                    type = SnackBarType.ERROR
                )
            }
        } else {
            viewModelScope.launch {
                updateMyInfoUseCase.invoke(
                    name = uiState.userInfo?.name ?: "",
                    jobOccupation = uiState.occupation ?: JobOccupation.OTHER,
                    locationIds = uiState.userInfo?.locations?.map { it.first } ?: emptyList(),
                    companyId = uiState.userInfo?.company?.id,
                    allowSameCompany = uiState.userInfo?.allowSameCompany
                ).mapMerge().collect { result ->
                    if (result != null) {
                        setEffect {
                            EditJobEffect.ShowToast(
                                message = "내 직군이 변경되었어요",
                                type = SnackBarType.DEFAULT
                            )
                        }
                        setEffect { EditJobEffect.NavigateToProfile(true) }
                    } else if (!isLoading) {
                        setEffect {
                            EditJobEffect.ShowToast(
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