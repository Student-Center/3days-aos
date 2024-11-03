package com.weave.my_profile.nick

import android.content.Context
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

sealed class NickAction : UIAction {
    data class SetNickName(val nickname: String) : NickAction()
    data object ValidateNickState : NickAction()
}

sealed class NickIntent : UIIntent {
    data class SetNickName(val nickname: String) : NickIntent()
    data object ValidateNickState : NickIntent()
}

data class NickState(
    val errorMessage: String = "",
    val nickname: String = ""
) : UIState

sealed class NickEffect : UIEffect {
    data object NavigateToNextScreen : NickEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : NickEffect()
}

@HiltViewModel
class MyProfileNickViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel<NickAction, NickIntent, NickState, NickEffect>(
    initialState = NickState()
) {
    override fun actionPredicate(action: NickAction): NickIntent {
        return when (action) {
            is NickAction.SetNickName -> NickIntent.SetNickName(action.nickname)
            is NickAction.ValidateNickState -> NickIntent.ValidateNickState
        }
    }

    override fun collectIntent(intent: NickIntent) {
        when (intent) {
            is NickIntent.SetNickName -> setNickName(intent.nickname)
            is NickIntent.ValidateNickState -> validateNickState()
        }
    }

    private fun setNickName(nickname: String) {
        setState { copy(nickname = nickname) }
    }

    private fun validateNickState() {
        if (uiState.nickname.length >= 2) {
            setEffect { NickEffect.NavigateToNextScreen }
        } else {
            setEffect {
                NickEffect.ShowToast(
                    message = context.getString(R.string.my_profile_nick_not_input_error_message),
                    type = SnackBarType.ERROR
                )
            }
        }
    }
}