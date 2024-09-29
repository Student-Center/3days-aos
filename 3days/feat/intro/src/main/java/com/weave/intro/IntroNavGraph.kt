package com.weave.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.navGraphIntro(navController: NavController) {
    navigation(startDestination = "welcome", route = "intro") {
        composable("welcome") { IntroScreen(navController) }
        composable("mobile_auth") { MobileAuthScreen() }
    }
}