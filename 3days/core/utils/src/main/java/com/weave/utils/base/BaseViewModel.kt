package com.weave.utils.base

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * 사용자의 직접적인 상호작용으로만 트리거될 수 있는 UI 액션을 나타내는 인터페이스.
 */
interface UIAction

/**
 * ViewModel의 흐름을 관리하기 위한 인터페이스.
 */
interface UIIntent

/**
 * 사용자에게 직접적으로 보여지는 UI 상태를 나타내는 인터페이스.
 */
interface UIState

/**
 * 네비게이션, 다이얼로그 등과 같이 사용자에게 직접적으로 보이지 않는 UX 부분을 관리하기 위한 인터페이스.
 */
interface UIEffect

abstract class BaseViewModel<Action : UIAction, Intent : UIIntent, State : UIState, Effect : UIEffect>(
    initialState: State
) : ViewModel() {

    private var _uiState by mutableStateOf(initialState)
    val uiState: State get() = _uiState

    private val _uiAction = MutableSharedFlow<Action>()
    private val _uiEffect = Channel<Effect>(Channel.BUFFERED)

    val uiEffect: Flow<Effect> = _uiEffect.receiveAsFlow()

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading

    private var _error by mutableStateOf("")
    val error: String get() = _error

    init {
        collectActions()
    }

    private fun collectActions() {
        viewModelScope.launch {
            _uiAction.collect { action ->
                val intent = actionPredicate(action)
                collectIntent(intent)
            }
        }
    }

    abstract fun actionPredicate(action: Action): Intent

    abstract fun collectIntent(intent: Intent)

    fun setState(transform: State.() -> State) {
        _uiState = uiState.transform()
    }

    protected fun withState(block: suspend State.() -> Unit) {
        viewModelScope.launch {
            uiState.block()
        }
    }

    fun setEffect(transform: () -> Effect) {
        viewModelScope.launch {
            val newEffect = transform()
            _uiEffect.send(newEffect)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <T> Flow<NetworkResult<T>>.mapMerge(): Flow<T?> = flatMapConcat { result ->
        Log.d("NetworkResult", "$result")
        when (result) {
            is NetworkResult.Loading -> {
                _isLoading = true
                flowOf(null)
            }
            is NetworkResult.Success -> {
                _isLoading = false
                flowOf(result.data)
            }
            is NetworkResult.Error -> {
                _isLoading = false
                _error = "[${result.error.code}] ${result.error.errorCode}"
                flowOf(null)
            }
        }
    }

    fun clearError() {
        _error = ""
    }

    fun setAction(action: Action) {
        viewModelScope.launch {
            _uiAction.emit(action)
        }
    }

    fun setIntent(intent: Intent) {
        viewModelScope.launch {
            collectIntent(intent)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _uiEffect.close()
    }
}
