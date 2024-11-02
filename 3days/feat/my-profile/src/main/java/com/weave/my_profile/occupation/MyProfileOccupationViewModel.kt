package com.weave.my_profile.occupation

import android.content.Context
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

sealed class OccupationAction : UIAction {
    data class SelectOccupation(val occupation: JobOccupation?) : OccupationAction()
    data object ValidateOccupationState : OccupationAction()
}

sealed class OccupationIntent : UIIntent {
    data class SelectOccupation(val occupation: JobOccupation?) : OccupationIntent()
    data object ValidateOccupationState : OccupationIntent()
}

data class OccupationState(
    val errorMessage: String = "",
    val selectedOccupation: JobOccupation? = null,
    val isChecked: Boolean = false,
) : UIState

sealed class OccupationEffect : UIEffect {
    data object NavigateToNextScreen : OccupationEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : OccupationEffect()
}

@HiltViewModel
class MyProfileOccupationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel<OccupationAction, OccupationIntent, OccupationState, OccupationEffect>(
    initialState = OccupationState()
) {
    override fun actionPredicate(action: OccupationAction): OccupationIntent {
        return when (action) {
            is OccupationAction.SelectOccupation -> OccupationIntent.SelectOccupation(action.occupation)
            is OccupationAction.ValidateOccupationState -> OccupationIntent.ValidateOccupationState
        }
    }

    override fun collectIntent(intent: OccupationIntent) {
        when (intent) {
            is OccupationIntent.SelectOccupation -> selectOccupation(intent.occupation)
            is OccupationIntent.ValidateOccupationState -> validateOccupationState()
        }
    }

    private fun selectOccupation(occupation: JobOccupation?) {
        setState { copy(selectedOccupation = occupation, isChecked = false) }
    }

    private fun validateOccupationState() {
        if (uiState.selectedOccupation != null) {
            setEffect { OccupationEffect.NavigateToNextScreen }
        } else {
            setEffect {
                OccupationEffect.ShowToast(
                    message = context.getString(R.string.my_profile_occupation_not_selected_error_message),
                    type = SnackBarType.ERROR
                )
            }
        }
    }
}