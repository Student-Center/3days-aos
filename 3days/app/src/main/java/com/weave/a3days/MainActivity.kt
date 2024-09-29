package com.weave.a3days

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.weave.design_system.DaysTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            !viewModel.uiState.value.isDataLoaded
        }

        setContent {
            val state by viewModel.uiState.collectAsState()

            if (state.isDataLoaded) {
                DaysTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = DaysTheme.colors.background
                    ) {
                        val navController = rememberNavController()
                        DaysNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
