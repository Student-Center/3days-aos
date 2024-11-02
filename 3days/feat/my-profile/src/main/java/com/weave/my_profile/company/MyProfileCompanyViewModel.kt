package com.weave.my_profile.company

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.weave.company.SearchCompaniesUseCase
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.model.domain.myprofile.Company
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

sealed class CompanyAction : UIAction {
    data class SearchCompanies(val keyword: String) : CompanyAction()
    data class GetNextPage(val keyword: String) : CompanyAction()
    data class SelectCompany(val company: Company) : CompanyAction()
    data class SetChecked(val isChecked: Boolean) : CompanyAction()
    data object ValidateInput : CompanyAction()
}

sealed class CompanyIntent : UIIntent {
    data class SearchCompanies(val keyword: String) : CompanyIntent()
    data class GetNextPage(val keyword: String) : CompanyIntent()
    data class SelectCompany(val company: Company) : CompanyIntent()
    data class SetChecked(val isChecked: Boolean) : CompanyIntent()
    data object ValidateInput : CompanyIntent()
}

data class CompanyState(
    val errorMessage: String = "",
    val companies: List<Company> = listOf(),
    val selectedCompany: Company? = null,
    val isChecked: Boolean = false,
    val nextUUID: UUID? = null
) : UIState

sealed class CompanyEffect : UIEffect {
    data object NavigateToNextScreen : CompanyEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : CompanyEffect()
    data object ShowBottomSheet : CompanyEffect()
}

@HiltViewModel
class MyProfileCompanyViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val searchCompaniesUseCase: SearchCompaniesUseCase
) : BaseViewModel<CompanyAction, CompanyIntent, CompanyState, CompanyEffect>(initialState = CompanyState()) {
    override fun actionPredicate(action: CompanyAction): CompanyIntent {
        return when (action) {
            is CompanyAction.SearchCompanies -> CompanyIntent.SearchCompanies(action.keyword)
            is CompanyAction.GetNextPage -> CompanyIntent.GetNextPage(action.keyword)
            is CompanyAction.SelectCompany -> CompanyIntent.SelectCompany(action.company)
            is CompanyAction.SetChecked -> CompanyIntent.SetChecked(action.isChecked)
            is CompanyAction.ValidateInput -> CompanyIntent.ValidateInput
        }
    }

    override fun collectIntent(intent: CompanyIntent) {
        when (intent) {
            is CompanyIntent.SearchCompanies -> debouncedSearch(intent.keyword)
            is CompanyIntent.GetNextPage -> search(intent.keyword)
            is CompanyIntent.SelectCompany -> selectCompany(intent.company)
            is CompanyIntent.SetChecked -> setChecked(intent.isChecked)
            is CompanyIntent.ValidateInput -> validateInput()
        }
    }

    private fun validateInput() {
        if (uiState.isChecked || uiState.selectedCompany != null) {
            setEffect { CompanyEffect.ShowBottomSheet }
        } else {
            setState { copy(errorMessage = context.getString(R.string.my_profile_company_not_selected_error_message)) }
            setEffect {
                CompanyEffect.ShowToast(
                    context.getString(R.string.my_profile_company_not_selected_error_message),
                    SnackBarType.ERROR
                )
            }
        }
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
                            CompanyEffect.ShowToast(
                                error,
                                SnackBarType.ERROR
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
}