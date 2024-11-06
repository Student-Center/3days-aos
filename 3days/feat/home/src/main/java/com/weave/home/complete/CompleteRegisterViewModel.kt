package com.weave.home.complete

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
import com.weave.model.domain.user.MyInfo
import com.weave.user.GetMyInfoUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CompleteRegisterAction : UIAction {
    data object FetchMyInfo : CompleteRegisterAction()
}

sealed class CompleteRegisterIntent : UIIntent {
    data object FetchMyInfo : CompleteRegisterIntent()
}

data class CompleteRegisterState(
    val errorMessage: String = "",
    val myInfo: MyInfo? = null,
) : UIState

sealed class CompleteRegisterEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : CompleteRegisterEffect()
}

@HiltViewModel
class CompleteRegisterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getMyInfoUseCase: GetMyInfoUseCase
) :
    BaseViewModel<CompleteRegisterAction, CompleteRegisterIntent, CompleteRegisterState, CompleteRegisterEffect>(
        initialState = CompleteRegisterState()
    ) {
    override fun actionPredicate(action: CompleteRegisterAction): CompleteRegisterIntent {
        return when (action) {
            is CompleteRegisterAction.FetchMyInfo -> CompleteRegisterIntent.FetchMyInfo
        }
    }

    override fun collectIntent(intent: CompleteRegisterIntent) {
        when (intent) {
            is CompleteRegisterIntent.FetchMyInfo -> fetchMyInfo()
        }
    }

    private fun fetchMyInfo() {
        viewModelScope.launch {
            getMyInfoUseCase.invoke().mapMerge().collect { myInfo ->
                if (myInfo != null) {
                    setState { copy(myInfo = myInfo) }

                } else if (!isLoading) {
                    setEffect {
                        CompleteRegisterEffect.ShowToast(
                            message = "내 정보 조회에 실패했습니다",
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }
}