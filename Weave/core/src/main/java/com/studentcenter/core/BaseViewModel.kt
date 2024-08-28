package com.studentcenter.core

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentcenter.core.model.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


/**
 * User의 직접적인 액션으로만 바꿀 수 있습니다.
 */
interface UIAction

/**
 * 세부적인 VM의 흐름을 관리합니다.
 */
interface UIIntent

/**
 * 유저에게 직접적으로 보여지는 UI를 담당합니다.
 */
interface UIState

/**
 * 유저가 보지 못하는 UX 적인 부분을 담당합니다. ( ex. Navigation, Dialog, ..etc )
 */
interface UIEffect

abstract class BaseViewModel <Action : UIAction, Intent: UIIntent, State : UIState, Effect : UIEffect>(
    initialState: State
) : ViewModel() {

    private var _uiState by mutableStateOf(initialState)
    val uiState get() = _uiState

    private val _uiAction: MutableSharedFlow<Action> = MutableSharedFlow()
    private val uiAction = _uiAction

    private val _uiEffect: MutableSharedFlow<Pair<Effect, Long>> = MutableSharedFlow()
    val uiEffect: SharedFlow<Pair<Effect, Long>> = _uiEffect

    private var _isLoading by mutableStateOf(false)
    val isLoading get() = _isLoading

    private var _error by mutableStateOf("")
    val error get() = _error

    init {
        viewModelScope.launch {
            uiAction.collect {
                collectIntent(actionPredicate(it))
            }
        }
    }

    abstract fun actionPredicate(action: Action): Intent

    abstract fun collectIntent(intent: Intent)

    fun setState(transform: State.() -> State) {
        _uiState = transform(uiState)
    }

    protected fun withState(block: suspend State.() -> Unit) {
        viewModelScope.launch {
            block(uiState)
        }
    }

    fun setEffect(transform: () -> Effect) {
        viewModelScope.launch {
            val newEffect = transform()
            val timestamp = System.currentTimeMillis()
            _uiEffect.emit(Pair(newEffect, timestamp))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <T> Flow<NetworkResult<T>>.mapMerge(): Flow<T?> = flatMapConcat {
        Log.d("NetworkResult", "$it")
        when (it) {
            is NetworkResult.Loading -> {
                _isLoading = true
                flowOf(null)
            }
            is NetworkResult.Success -> {
                _isLoading = false
                flowOf(it.data)
            }
            is NetworkResult.Error -> {
                _isLoading = false
                _error = "[${it.error.code}] ${it.error.errorCode}"
                flowOf(null)
            }
        }
    }

    fun clearError() {
        viewModelScope.launch {
            _error = ""
        }
    }

    fun setAction(action: Action) {
        viewModelScope.launch {
            _uiAction.emit(action)
        }
    }
    fun setIntent(intent: Intent) = viewModelScope.launch {
        collectIntent(intent)
    }
}