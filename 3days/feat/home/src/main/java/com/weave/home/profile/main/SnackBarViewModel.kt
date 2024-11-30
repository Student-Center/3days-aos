package com.weave.home.profile.main

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.design_system.component.SnackBarType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnackBarViewModel : ViewModel() {
    val snackBarHostState = SnackbarHostState()

    fun showSnackBar(message: String, type: SnackBarType) {
        viewModelScope.launch {
            val job = launch {
                snackBarHostState.showSnackbar(
                    message = message,
                    actionLabel = type.toString(),
                    duration = SnackbarDuration.Indefinite
                )
            }
            delay(3000L)
            job.cancel()
        }
    }
}