package com.weave.my_profile

import com.weave.design_system.component.SnackBarType
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Year
import javax.inject.Inject

sealed class BirthYearAction : UIAction {
    data object ValidateBirthYear : BirthYearAction()
    data class SetBirthYear(val index: Int, val value: String) : BirthYearAction()
}

sealed class BirthYearIntent : UIIntent {
    data object ValidateBirthYear : BirthYearIntent()
    data class SetBirthYear(val index: Int, val value: String) : BirthYearIntent()
}

data class BirthYearState(
    val errorMessage: String = "",
    val birthYear: List<String> = listOf("", "", "", ""),
    val invalidBirthYearFlag: Boolean = false
) : UIState

sealed class BirthYearEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : BirthYearEffect()
    data object NavigateToNextScreen : BirthYearEffect()

}

@HiltViewModel
class MyProfileBirthYearViewModel @Inject constructor(

) : BaseViewModel<BirthYearAction, BirthYearIntent, BirthYearState, BirthYearEffect>(initialState = BirthYearState()) {

    override fun actionPredicate(action: BirthYearAction): BirthYearIntent {
        return when (action) {
            is BirthYearAction.ValidateBirthYear -> BirthYearIntent.ValidateBirthYear
            is BirthYearAction.SetBirthYear -> BirthYearIntent.SetBirthYear(
                action.index,
                action.value
            )
        }
    }

    override fun collectIntent(intent: BirthYearIntent) {
        when (intent) {
            is BirthYearIntent.ValidateBirthYear -> validateBirthYear()
            is BirthYearIntent.SetBirthYear -> setBirthYear(intent.index, intent.value)
        }
    }

    private fun setBirthYear(index: Int, newValue: String) {
        setState { copy(invalidBirthYearFlag = false) }

        val updatedBirthYear = uiState.birthYear.toMutableList()

        updatedBirthYear[index] = newValue

        setState {
            copy(birthYear = updatedBirthYear)
        }
    }


    private fun validateBirthYear() {
        if (uiState.birthYear.all { it.isBlank() }) {
            setState { copy(invalidBirthYearFlag = true) }
            setEffect {
                BirthYearEffect.ShowToast(
                    message = "나이를 입력해 주세요",
                    type = SnackBarType.ERROR
                )
            }

            return
        }

        val currentYear = Year.now().value
        val minYear = currentYear - 35
        val maxYear = currentYear - 20

        val result = uiState.birthYear.joinToString("").toInt() in minYear..maxYear

        setState { copy(invalidBirthYearFlag = true) }
        setEffect {
            if (result) BirthYearEffect.NavigateToNextScreen
            else BirthYearEffect.ShowToast(
                "가입이 불가한 연령이예요",
                SnackBarType.ERROR
            )
        }
    }

}