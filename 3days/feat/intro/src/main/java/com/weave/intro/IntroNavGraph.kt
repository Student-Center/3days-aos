package com.weave.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

enum class Route(val routeName: String) {
    Intro("intro"),
    Welcome("welcome"),
    MobileSendAuth("mobile_send_auth"),
    MobileEnterAuth("mobile_enter_auth"),
    TermsAgreement("terms_agreement"),
    Main("main"),
    MyProfile("my_profile");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg -> append("/$arg") }
        }
    }
}

fun NavGraphBuilder.navGraphIntro(navController: NavController) {
    navigation(startDestination = Route.Welcome.routeName, route = Route.Intro.routeName) {
        composable(Route.Welcome.routeName) {
            // 개발 편의를 위한 주석 처리
//            IntroScreen(onClicked = { navController.navigate(Route.MobileSendAuth.routeName) })
            IntroScreen(onClicked = { navController.navigate(Route.MyProfile.withArgs("Test Register Token")) })
        }

        composable(Route.MobileSendAuth.routeName) {
            MobileSendAuthScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { mobileNum ->
                    navController.navigate(Route.MobileEnterAuth.withArgs(mobileNum))
                }
            )
        }

        composable(
            route = Route.MobileEnterAuth.withArgs("{mobileNum}"),
            arguments = listOf(navArgument("mobileNum") { type = NavType.StringType })
        ) { backStackEntry ->
            val mobileNum = backStackEntry.arguments?.getString("mobileNum") ?: ""
            MobileEnterAuthScreen(
                mobileNum = mobileNum,
                onBackBtnClicked = { navController.popBackStack() },
                navigateToMainScreen = { navController.navigate(Route.Main.routeName) },
                navigateToRegisterFlow = { registerToken ->
                    navController.navigate(Route.TermsAgreement.withArgs(registerToken))
                }
            )
        }

        composable(
            route = Route.TermsAgreement.withArgs("{registerToken}"),
            arguments = listOf(navArgument("registerToken") { type = NavType.StringType })
        ) { backStackEntry ->
            val registerToken = backStackEntry.arguments?.getString("registerToken") ?: ""
            TermsAgreementScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = {
                    navController.navigate(Route.MyProfile.withArgs(registerToken)) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}