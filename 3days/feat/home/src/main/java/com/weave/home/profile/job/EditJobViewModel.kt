package com.weave.home.profile.job

import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
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
    data class FetchData(val occupation: JobOccupation) : EditJobAction()
    data class UpdateData(val occupation: JobOccupation) : EditJobAction()
    data object RequestUpdate : EditJobAction()
}

sealed class EditJobIntent : UIIntent {
    data class FetchData(val occupation: JobOccupation) : EditJobIntent()
    data class UpdateData(val occupation: JobOccupation) : EditJobIntent()
    data object RequestUpdate : EditJobIntent()
}

data class EditJobState(
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
            is EditJobAction.FetchData -> EditJobIntent.FetchData(action.occupation)
            is EditJobAction.UpdateData -> EditJobIntent.UpdateData(action.occupation)
            is EditJobAction.RequestUpdate -> EditJobIntent.RequestUpdate
        }
    }

    override fun collectIntent(intent: EditJobIntent) {
        when (intent) {
            is EditJobIntent.FetchData -> fetchData(intent.occupation)
            is EditJobIntent.UpdateData -> updateData(intent.occupation)
            is EditJobIntent.RequestUpdate -> requestUpdate()
        }
    }

    private fun fetchData(data: JobOccupation) = setState {
        copy(
            initOccupation = data,
            occupation = data
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
                    jobOccupation = uiState.occupation
                ).mapMerge().collect { result ->
                    if (result != null) {
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