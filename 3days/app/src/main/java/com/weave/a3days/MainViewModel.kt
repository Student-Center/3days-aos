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
    val isSplashComplete: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(1000L) // 추후 로그인 여부 체크 로직으로 수정
            _uiState.value = _uiState.value.copy(isDataLoaded = true)
        }
    }
}