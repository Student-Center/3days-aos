package com.weave.my_profile.partner.distance

import android.content.Context
import com.weave.design_system.component.SnackBarType
import com.weave.model.enum.PreferDistance
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

sealed class PartnerDistanceAction : UIAction {
    data class SetDistance(val distance: PreferDistance) : PartnerDistanceAction()
    data object ValidateInput : PartnerDistanceAction()
}

sealed class PartnerDistanceIntent : UIIntent {
    data class SetDistance(val distance: PreferDistance) : PartnerDistanceIntent()
    data object ValidateInput : PartnerDistanceIntent()
}

data class PartnerDistanceState(
    val errorMessage: String = "",
    val distance: PreferDistance? = null,
) : UIState

sealed class PartnerDistanceEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : PartnerDistanceEffect()
    data object NavigateToNextScreen : PartnerDistanceEffect()

}

@HiltViewModel
class PartnerDistanceViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel<PartnerDistanceAction, PartnerDistanceIntent, PartnerDistanceState, PartnerDistanceEffect>(
    initialState = PartnerDistanceState()
) {

    override fun actionPredicate(action: PartnerDistanceAction): PartnerDistanceIntent {
        return when (action) {
            is PartnerDistanceAction.SetDistance -> PartnerDistanceIntent.SetDistance(action.distance)
            is PartnerDistanceAction.ValidateInput -> PartnerDistanceIntent.ValidateInput
        }
    }

    override fun collectIntent(intent: PartnerDistanceIntent) {
        when (intent) {
            is PartnerDistanceIntent.SetDistance -> setDistance(intent.distance)
            is PartnerDistanceIntent.ValidateInput -> validateInput()
        }
    }

    private fun setDistance(distance: PreferDistance) {
        val value = if (uiState.distance == distance) null else distance
        setState { copy(distance = value) }
    }

    private fun validateInput() {
        val effect = uiState.distance?.let {
            PartnerDistanceEffect.NavigateToNextScreen
        } ?: PartnerDistanceEffect.ShowToast(
            message = context.getString(com.weave.design_system.R.string.partner_distance_not_selected_error_message),
            type = SnackBarType.ERROR
        )

        setEffect { effect }
    }
}