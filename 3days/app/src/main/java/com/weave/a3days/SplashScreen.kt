package com.weave.a3days

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onDataLoadedResult: (Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isDataLoaded) {
        if(uiState.isDataLoaded){
            onDataLoadedResult(uiState.isValid)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .background(DaysTheme.colors.bgSplashBrown),
            painter = painterResource(id = com.weave.design_system.R.drawable.texture_bg),
            contentDescription = "Splash Background"
        )

        Image(
            modifier = Modifier.size(137.dp, 97.dp),
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Splash Logo"
        )
    }
}