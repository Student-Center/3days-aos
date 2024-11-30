package com.weave.home.profile.company

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.weave.company.SearchCompaniesUseCase
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.home.profile.UserInfo
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.user.UpdateMyInfoUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import com.weave.utils.network.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class EditCompanyAction : UIAction {
    data class FetchData(val userInfo: UserInfo) : EditCompanyAction()
    data class SearchCompanies(val keyword: String) : EditCompanyAction()
    data class GetNextPage(val keyword: String) : EditCompanyAction()
    data class SelectCompany(val company: Company) : EditCompanyAction()
    data class SetChecked(val isChecked: Boolean) : EditCompanyAction()
    data object ValidateInput : EditCompanyAction()
    data class UpdateData(val allowSameCompany: Boolean) : EditCompanyAction()
}

sealed class EditCompanyIntent : UIIntent {
    data class FetchData(val userInfo: UserInfo) : EditCompanyIntent()
    data class SearchCompanies(val keyword: String) : EditCompanyIntent()
    data class GetNextPage(val keyword: String) : EditCompanyIntent()
    data class SelectCompany(val company: Company) : EditCompanyIntent()
    data class SetChecked(val isChecked: Boolean) : EditCompanyIntent()
    data object ValidateInput : EditCompanyIntent()
    data class UpdateData(val allowSameCompany: Boolean) : EditCompanyIntent()
}

data class EditCompanyState(
    val userInfo: UserInfo? = null,
    val companies: List<Company> = listOf(),
    val selectedCompany: Company? = null,
    val isChecked: Boolean = false,
    val nextUUID: UUID? = null,
    val initCompany: Company? = null
) : UIState

sealed class EditCompanyEffect : UIEffect {
    data class NavigateToProfile(val isSuccess: Boolean) : EditCompanyEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : EditCompanyEffect()
    data object ShowBottomSheet : EditCompanyEffect()
}

@HiltViewModel
class EditCompanyViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val updateMyInfoUseCase: UpdateMyInfoUseCase,
    private val searchCompaniesUseCase: SearchCompaniesUseCase
) : BaseViewModel<EditCompanyAction, EditCompanyIntent, EditCompanyState, EditCompanyEffect>(
    initialState = EditCompanyState()
) {

    override fun actionPredicate(action: EditCompanyAction): EditCompanyIntent {
        return when (action) {
            is EditCompanyAction.FetchData -> EditCompanyIntent.FetchData(action.userInfo)
            is EditCompanyAction.SearchCompanies -> EditCompanyIntent.SearchCompanies(action.keyword)
            is EditCompanyAction.GetNextPage -> EditCompanyIntent.GetNextPage(action.keyword)
            is EditCompanyAction.SelectCompany -> EditCompanyIntent.SelectCompany(action.company)
            is EditCompanyAction.SetChecked -> EditCompanyIntent.SetChecked(action.isChecked)
            is EditCompanyAction.ValidateInput -> EditCompanyIntent.ValidateInput
            is EditCompanyAction.UpdateData -> EditCompanyIntent.UpdateData(action.allowSameCompany)
        }
    }

    override fun collectIntent(intent: EditCompanyIntent) {
        when (intent) {
            is EditCompanyIntent.FetchData -> fetchData(intent.userInfo)
            is EditCompanyIntent.SearchCompanies -> debouncedSearch(intent.keyword)
            is EditCompanyIntent.GetNextPage -> search(intent.keyword)
            is EditCompanyIntent.SelectCompany -> selectCompany(intent.company)
            is EditCompanyIntent.SetChecked -> setChecked(intent.isChecked)
            is EditCompanyIntent.ValidateInput -> validateInput()
            is EditCompanyIntent.UpdateData -> updateData(intent.allowSameCompany)
        }
    }

    private fun fetchData(data: UserInfo?) = setState {
        copy(
            userInfo = data,
            initCompany = data?.company,
            isChecked = data?.company == null
        )
    }

    private fun selectCompany(company: Company) {
        setState { copy(selectedCompany = company, isChecked = false) }
    }

    private fun setChecked(isChecked: Boolean) {
        setState {
            copy(
                selectedCompany = null,
                isChecked = isChecked
            )
        }
    }

    private fun search(keyword: String) {
        viewModelScope.launch {
            searchCompaniesUseCase.invoke(keyword = keyword, next = uiState.nextUUID).mapMerge()
                .collect {
                    if (it != null) {
                        val newList = uiState.companies.toMutableList()
                        newList.addAll(it.data)

                        setState {
                            copy(
                                companies = newList,
                                nextUUID = it.next
                            )
                        }
                    } else if (!isLoading) {
                        setEffect {
                            EditCompanyEffect.ShowToast(
                                message = "다시 시도해 주세요",
                                type = SnackBarType.ERROR
                            )
                        }
                    }
                }
        }
    }

    private val debouncedSearch = debounce<String>(
        timeMillis = 500L,
        coroutineScope = viewModelScope
    ) { query ->
        setState {
            copy(
                companies = listOf(),
                nextUUID = null
            )
        }
        search(keyword = query)
    }

    private fun validateInput() {
        if (uiState.isChecked || uiState.selectedCompany != null) {
            setEffect { EditCompanyEffect.ShowBottomSheet }
        } else {
            setEffect {
                EditCompanyEffect.ShowToast(
                    context.getString(R.string.my_profile_company_not_selected_error_message),
                    SnackBarType.ERROR
                )
            }
        }
    }

    private fun updateData(allowSameCompany: Boolean) {
        viewModelScope.launch {
            updateMyInfoUseCase.invoke(
                name = uiState.userInfo?.name ?: "",
                jobOccupation = uiState.userInfo?.jobOccupation ?: JobOccupation.OTHER,
                locationIds = uiState.userInfo?.locations?.map { it.first } ?: emptyList(),
                companyId = uiState.selectedCompany?.id,
                allowSameCompany = allowSameCompany
            ).mapMerge().collect { result ->
                if (result != null) {
                    setEffect {
                        EditCompanyEffect.ShowToast(
                            message = "내 회사가 변경되었어요",
                            type = SnackBarType.DEFAULT
                        )
                    }
                    setEffect { EditCompanyEffect.NavigateToProfile(true) }
                } else if (!isLoading) {
                    setEffect {
                        EditCompanyEffect.ShowToast(
                            message = "다시 시도해 주세요",
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }
}