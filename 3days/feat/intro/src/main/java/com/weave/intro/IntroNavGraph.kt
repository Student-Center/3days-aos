package com.weave.intro

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
                onNextBtnClicked = { navController.navigate("terms_agreement") }
            )
        }

        composable("terms_agreement"){
            TermsAgreementScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { navController.navigate("my_profile") }
            )
        }
    }
}