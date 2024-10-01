package com.weave.a3days

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(
    val isDataLoaded: Boolean = false,
    val isValid: Boolean = false,
)

@HiltViewModel
class SplashViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(1000L) // 추후 로그인 여부 체크 로직으로 수정
private fun loadData() {
    viewModelScope.launch {
        delay(1000L) // TODO: 실제 데이터 로딩 로직으로 대체 필요. 현재는 로딩 화면을 보여주기 위한 임시 지연
        _uiState.value = _uiState.value.copy(
            isDataLoaded = true,
            isValid = false // TODO: 실제 사용자 인증 상태를 반영하도록 수정 필요
        )
    }
}
        }
    }
}