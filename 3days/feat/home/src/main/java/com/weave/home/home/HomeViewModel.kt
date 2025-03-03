package com.weave.home.home

import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
import com.weave.home.profile.UserInfo
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileImage
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.user.DeleteProfileImageUseCase
import com.weave.user.DeleteProfileWidgetUseCase
import com.weave.user.GetMyInfoUseCase
import com.weave.user.PutProfileWidgetUseCase
import com.weave.user.UploadProfileImageUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

sealed class HomeAction : UIAction {

}

sealed class HomeIntent : UIIntent {

}

data class HomeState(
    val dummy: String = ""
) : UIState

sealed class HomeEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : HomeEffect()

}

@HiltViewModel
class HomeViewModel @Inject constructor(

) : BaseViewModel<HomeAction, HomeIntent, HomeState, HomeEffect>(initialState = HomeState()) {

    override fun actionPredicate(action: HomeAction): HomeIntent {
        TODO()
    }

    override fun collectIntent(intent: HomeIntent) {
        TODO()
    }
}