package com.weave.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.weave.auth.PostAuthPhoneUseCase
import com.weave.model.network.NetworkResult
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthAction : UIAction {
    data class SubmitPhoneNumber(val phoneNumber: String) : AuthAction()
}

sealed class AuthIntent : UIIntent {
    data class PostPhoneNumber(val phoneNumber: String) : AuthIntent()
}

data class AuthState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UIState

sealed class AuthEffect : UIEffect {
    data object Success : AuthEffect()
    data class ShowError(val message: String) : AuthEffect()
}


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val postAuthPhoneUseCase: PostAuthPhoneUseCase
) : BaseViewModel<AuthAction, AuthIntent, AuthState, AuthEffect>(
    initialState = AuthState()
) {

    override fun actionPredicate(action: AuthAction): AuthIntent {
        return when (action) {
            is AuthAction.SubmitPhoneNumber -> AuthIntent.PostPhoneNumber(action.phoneNumber)
        }
    }

    override fun collectIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.PostPhoneNumber -> {
                viewModelScope.launch {
                    postAuthPhoneUseCase.invoke(intent.phoneNumber).mapMerge().collect {
                        if(it != null){
                            setEffect { AuthEffect.Success }
                        }
                        else if (!isLoading){
                            setEffect { AuthEffect.ShowError(error) }
                        }
                    }
                }
            }
        }
    }
}