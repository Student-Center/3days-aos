package com.weave.my_profile.partner.age

import com.weave.design_system.component.SnackBarType
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class PartnerAgeAction : UIAction {
    data class SetUnderAge(val age: String) : PartnerAgeAction()
    data class SetUpperAge(val age: String) : PartnerAgeAction()
}

sealed class PartnerAgeIntent : UIIntent {
    data class SetUnderAge(val age: String) : PartnerAgeIntent()
    data class SetUpperAge(val age: String) : PartnerAgeIntent()
}

data class PartnerAgeState(
    val errorMessage: String = "",
    val underAge: String = "",
    val upperAge: String = ""
) : UIState

sealed class BirthYearEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : BirthYearEffect()
    data object NavigateToNextScreen : BirthYearEffect()

}

@HiltViewModel
class PartnerAgeViewModel @Inject constructor(

) : BaseViewModel<PartnerAgeAction, PartnerAgeIntent, PartnerAgeState, BirthYearEffect>(initialState = PartnerAgeState()) {

    override fun actionPredicate(action: PartnerAgeAction): PartnerAgeIntent {
        return when (action) {
            is PartnerAgeAction.SetUpperAge -> PartnerAgeIntent.SetUpperAge(action.age)
            is PartnerAgeAction.SetUnderAge -> PartnerAgeIntent.SetUnderAge(action.age)
        }
    }

    override fun collectIntent(intent: PartnerAgeIntent) {
        when (intent) {
            is PartnerAgeIntent.SetUpperAge -> setUpperAge(intent.age)
            is PartnerAgeIntent.SetUnderAge -> setUnderAge(intent.age)
        }
    }

    private fun setUnderAge(age: String) = setState { copy(underAge = age) }
    private fun setUpperAge(age: String) = setState { copy(upperAge = age) }
}