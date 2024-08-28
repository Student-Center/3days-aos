package com.studentcenter.sample

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SampleScreen(
    modifier: Modifier = Modifier,
    viewModel: SampleViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val effect by viewModel.uiEffect.collectAsState(initial = null)

    LaunchedEffect(key1 = effect) {
        when (effect?.first){
            is LoginEffect.NavigateToHome -> {
                Log.d("Sample", "NavigateToHome")
            }
            is LoginEffect.ShowError -> {
                Log.d("EFFECT", "show error")
                Toast.makeText(context, (effect!!.first as LoginEffect.ShowError).message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            UsernameField(username = state.username) { newValue ->
                viewModel.setState { copy(username = newValue) }
            }
            PasswordField(password = state.password) { newValue ->
                viewModel.setState { copy(password = newValue) }
            }
            LoginButton(
                onClick = { viewModel.setAction(LoginAction.Submit) },
                isLoading = viewModel.isLoading
            )
        }
        LoadingIndicator()
    }
}

@Composable
fun UsernameField(username: String, onValueChange: (String) -> Unit) {
    TextField(
        value = username,
        onValueChange = onValueChange,
        label = { Text("Username") }
    )
}

@Composable
fun PasswordField(password: String, onValueChange: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = onValueChange,
        label = { Text("Password") }
    )
}

@Composable
fun LoginButton(onClick: () -> Unit, isLoading: Boolean) {
    Button(
        onClick = onClick,
        enabled = !isLoading
    ) {
        Text("Login")
    }
}

@Composable
fun LoadingIndicator(viewModel: SampleViewModel = hiltViewModel()) {
    if (viewModel.isLoading) {
        CircularProgressIndicator()
    }
}