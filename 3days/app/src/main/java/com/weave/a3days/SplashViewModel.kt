package com.weave.a3days

import androidx.lifecycle.viewModelScope
import com.weave.auth.GetTokensUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class SplashUiAction : UIAction {
    data object ValidateToken : SplashUiAction()
}

sealed class SplashUiIntent : UIIntent {
    data object ValidateToken : SplashUiIntent()
}

sealed class SplashUiEffect : UIEffect {
    data object NavigateToHome : SplashUiEffect()
    data object NavigateToIntro : SplashUiEffect()
}

data class SplashUiState(
    val isDataLoaded: Boolean = false,
    val isValid: Boolean = false,
) : UIState

@HiltViewModel
class SplashViewModel @Inject constructor(
//    private val getMyInfoUseCase: GetMyInfoUseCase
    private val getTokensUseCase: GetTokensUseCase
) : BaseViewModel<SplashUiAction, SplashUiIntent, SplashUiState, SplashUiEffect>(initialState = SplashUiState()) {

    override fun actionPredicate(action: SplashUiAction): SplashUiIntent {
        return when (action) {
            is SplashUiAction.ValidateToken -> SplashUiIntent.ValidateToken
        }
    }

    override fun collectIntent(intent: SplashUiIntent) {
        return when (intent) {
            is SplashUiIntent.ValidateToken -> validateToken()
        }
    }

    private fun validateToken() {
        viewModelScope.launch {
            val tokens = getTokensUseCase.invoke()

            if (tokens.accessToken.isNotBlank() && tokens.refreshToken.isNotBlank()) {
                setEffect { SplashUiEffect.NavigateToHome }
            } else {
                setEffect { SplashUiEffect.NavigateToIntro }
            }
        }
    }
}