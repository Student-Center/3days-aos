package com.weave.home.profile.main

import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
import com.weave.home.profile.UserInfo
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.user.DeleteProfileWidgetUseCase
import com.weave.user.GetMyInfoUseCase
import com.weave.user.PutProfileWidgetUseCase
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
    data class EditProfileWidget(val type: ProfileWidgetType, val content: String) : ProfileAction()
    data class AddProfileWidget(val type: ProfileWidgetType, val content: String) : ProfileAction()
    data class DeleteProfileWidget(val type: ProfileWidgetType) : ProfileAction()
}

sealed class ProfileIntent : UIIntent {
    data object FetchData : ProfileIntent()
    data class EditProfileWidget(val type: ProfileWidgetType, val content: String) : ProfileIntent()
    data class AddProfileWidget(val type: ProfileWidgetType, val content: String) : ProfileIntent()
    data class DeleteProfileWidget(val type: ProfileWidgetType) : ProfileIntent()
}

data class ProfileState(
    val userInfo: UserInfo? = null,
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
    data object DismissSheet : ProfileEffect()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val putProfileWidgetUseCase: PutProfileWidgetUseCase,
    private val deleteProfileWidgetUseCase: DeleteProfileWidgetUseCase
) : BaseViewModel<ProfileAction, ProfileIntent, ProfileState, ProfileEffect>(initialState = ProfileState()) {

    override fun actionPredicate(action: ProfileAction): ProfileIntent {
        return when (action) {
            is ProfileAction.FetchData -> ProfileIntent.FetchData
            is ProfileAction.AddProfileWidget -> ProfileIntent.AddProfileWidget(
                action.type,
                action.content
            )

            is ProfileAction.EditProfileWidget -> ProfileIntent.EditProfileWidget(
                action.type,
                action.content
            )

            is ProfileAction.DeleteProfileWidget -> ProfileIntent.DeleteProfileWidget(action.type)
        }
    }

    override fun collectIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.FetchData -> fetchData()
            is ProfileIntent.AddProfileWidget -> addProfileWidget(intent.type, intent.content)
            is ProfileIntent.EditProfileWidget -> editProfileWidget(intent.type, intent.content)
            is ProfileIntent.DeleteProfileWidget -> deleteProfileWidget(intent.type)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getMyInfoUseCase.invoke().mapMerge().collect { myInfo ->
                if (myInfo != null) {
                    setState {
                        copy(
                            userInfo = myInfo.toUserInfo,
                            name = myInfo.name,
                            birthYear = myInfo.profile.birthYear,
                            profileUrl = "",
                            occupation = myInfo.profile.jobOccupation,
                            company = myInfo.profile.company,
                            locations = myInfo.profile.locations,
                            profileWidgets = myInfo.profileWidgets.sortedBy {
                                ProfileWidgetType.entries.indexOf(
                                    it.type
                                )
                            }
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

    private fun editProfileWidget(type: ProfileWidgetType, content: String) {
        viewModelScope.launch {
            putProfileWidgetUseCase.invoke(type, content).mapMerge().collect { result ->
                if (result != null) {
                    val newProfileWidgets = uiState.profileWidgets
                    newProfileWidgets.find { it.type == result.type }?.content = result.content

                    setState {
                        copy(
                            profileWidgets = newProfileWidgets
                        )
                    }

                    setEffect {
                        ProfileEffect.ShowToast(
                            message = "위젯이 변경되었어요",
                            type = SnackBarType.DEFAULT
                        )
                    }

                    setEffect { ProfileEffect.DismissSheet }
                } else if (!isLoading) {
                    setEffect {
                        ProfileEffect.ShowToast(
                            message = "위젯 변경에 실패했습니다",
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun addProfileWidget(type: ProfileWidgetType, content: String) {
        viewModelScope.launch {
            putProfileWidgetUseCase.invoke(type, content).mapMerge().collect { result ->
                if (result != null) {
                    val newProfileWidgets = uiState.profileWidgets.toMutableList()
                    newProfileWidgets.add(result)

                    setState {
                        copy(
                            profileWidgets = newProfileWidgets
                        )
                    }

                    setEffect {
                        ProfileEffect.ShowToast(
                            message = "위젯이 추가되었어요",
                            type = SnackBarType.DEFAULT
                        )
                    }

                    setEffect { ProfileEffect.DismissSheet }
                } else if (!isLoading) {
                    setEffect {
                        ProfileEffect.ShowToast(
                            message = "위젯 추가에 실패했습니다",
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun deleteProfileWidget(type: ProfileWidgetType) {
        viewModelScope.launch {
            deleteProfileWidgetUseCase.invoke(type).mapMerge().collect { result ->
                if (result != null) {
                    val newProfileWidgets = uiState.profileWidgets.toMutableList()
                    newProfileWidgets.remove(
                        newProfileWidgets.find { it.type == type }
                    )

                    setState {
                        copy(
                            profileWidgets = newProfileWidgets
                        )
                    }

                    setEffect {
                        ProfileEffect.ShowToast(
                            message = "위젯이 삭제되었어요",
                            type = SnackBarType.DEFAULT
                        )
                    }
                } else if (!isLoading) {
                    setEffect {
                        ProfileEffect.ShowToast(
                            message = "위젯 삭제에 실패했습니다",
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }

    private val MyInfo.toUserInfo
        get() = UserInfo(
            name = this.name,
            jobOccupation = this.profile.jobOccupation,
            locations = this.profile.locations,
            company = this.profile.company,
            desiredPartner = this.desiredPartner
        )
}