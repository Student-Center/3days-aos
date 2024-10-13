package com.weave.intro

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

fun NavGraphBuilder.navGraphIntro(navController: NavController) {
    navigation(startDestination = "welcome", route = "intro") {
        composable("welcome") {
            IntroScreen(
                onClicked = { navController.navigate("mobile_send_auth") }
            )
        }
        composable("mobile_send_auth") {
            MobileSendAuthScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { navController.navigate("mobile_enter_auth/$it") }
            )
        }
        composable(
            route = "mobile_enter_auth/{mobileNum}",
            arguments = listOf(navArgument("mobileNum") { type = NavType.StringType })
        ) { backStackEntry ->
            val mobileNum = backStackEntry.arguments?.getString("mobileNum") ?: ""
            MobileEnterAuthScreen(
                mobileNum = mobileNum,
                onBackBtnClicked = { navController.popBackStack() },
                navigateToMainScreen = { navController.navigate("main") },
                navigateToRegisterFlow = { navController.navigate("terms_agreement/$it")}
            )
        }

        composable(
            route = "terms_agreement/{registerToken}",
            arguments = listOf(navArgument("registerToken") { type = NavType.StringType })
            ) { backStackEntry ->
            val registerToken = backStackEntry.arguments?.getString("registerToken") ?: ""
            TermsAgreementScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { navController.navigate("my_profile/$registerToken") }
            )
        }
    }
}