package com.studentcenter.sample

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.studentcenter.core.BaseViewModel
import com.studentcenter.core.UIAction
import com.studentcenter.core.UIEffect
import com.studentcenter.core.UIIntent
import com.studentcenter.core.UIState
import com.studentcenter.core.model.NetworkError
import com.studentcenter.core.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginAction: UIAction {
    data object Submit: LoginAction()
}

sealed class LoginIntent: UIIntent {
    data class SubmitLogin(val username: String, val password: String): LoginIntent()
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false
): UIState

sealed class LoginEffect: UIEffect {
    data object NavigateToHome: LoginEffect()
    data class ShowError(val message: String): LoginEffect()
}

@HiltViewModel
class SampleViewModel @Inject constructor() : BaseViewModel<LoginAction, LoginIntent, LoginState, LoginEffect>(
    initialState = LoginState()
){
    override fun actionPredicate(action: LoginAction): LoginIntent {
        return when(action){
            LoginAction.Submit -> {
                Log.d("ACTION", "submit")
                LoginIntent.SubmitLogin(
                    username = uiState.username,
                    password = uiState.password
                )
            }
        }
    }

    override fun collectIntent(intent: LoginIntent) {
        when(intent){
            is LoginIntent.SubmitLogin -> {
                submitLogin(intent.username, intent.password)
            }
        }
    }

    private fun submitLogin(username: String, password: String){
        viewModelScope.launch {
            val result = fakeApiCall(username, password)
            result.mapMerge().collect { networkResult ->
                if(networkResult != null){
                    setState { copy(isLoggedIn = true) }
                    setEffect { LoginEffect.NavigateToHome }
                } else if (!isLoading) {
                    setEffect { LoginEffect.ShowError(error) }
                }
            }
        }
    }

    private fun fakeApiCall(username: String, password: String): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading)

        delay(1000)

        if (username.isEmpty() || password.isEmpty()) {
            emit(NetworkResult.Error(NetworkError.INVALID_CALL))
        } else {
            emit(NetworkResult.Success(true))
        }
    }
}