package com.weave.home.profile

import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.ProfileWidget
import com.weave.user.GetMyInfoUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class ProfileAction : UIAction {
    data object FetchData : ProfileAction()
}

sealed class ProfileIntent : UIIntent {
    data object FetchData : ProfileIntent()
}

data class ProfileState(
    val name: String = "",
    val birthYear: Int? = null,
    val profileUrl: String = "",
    val occupation: JobOccupation? = null,
    val company: Company? = null,
    val locations: List<Pair<UUID, String>> = listOf(),
    val profileWidgets: List<ProfileWidget> = listOf()
) : UIState

sealed class ProfileEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : ProfileEffect()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase
) : BaseViewModel<ProfileAction, ProfileIntent, ProfileState, ProfileEffect>(initialState = ProfileState()) {

    override fun actionPredicate(action: ProfileAction): ProfileIntent {
        return when (action) {
            is ProfileAction.FetchData -> ProfileIntent.FetchData
        }
    }

    override fun collectIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getMyInfoUseCase.invoke().mapMerge().collect { myInfo ->
                if (myInfo != null) {
                    setState {
                        copy(
                            name = myInfo.name,
                            birthYear = myInfo.profile.birthYear,
                            profileUrl = "",
                            occupation = myInfo.profile.jobOccupation,
                            company = myInfo.profile.company,
                            locations = myInfo.profile.locations,
                            profileWidgets = myInfo.profileWidgets
                        )
                    }

                } else if (!isLoading) {
                    setEffect {
                        ProfileEffect.ShowToast(
                            message = "내 정보 조회에 실패했습니다",
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }
}