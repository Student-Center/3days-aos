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
    Home("home"),
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
            IntroScreen(onClicked = { navController.navigate(Route.MobileSendAuth.routeName) })
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
                navigateToMainScreen = { navController.navigate(Route.Home.routeName) },
                navigateToRegisterFlow = { registerToken ->
                    navController.navigate(Route.TermsAgreement.withArgs(registerToken, mobileNum))
                }
            )
        }

        composable(
            route = Route.TermsAgreement.withArgs("{registerToken}", "{mobileNum}"),
            arguments = listOf(
                navArgument("registerToken") { type = NavType.StringType },
                navArgument("mobileNum") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val registerToken = backStackEntry.arguments?.getString("registerToken") ?: ""
            val mobileNum = backStackEntry.arguments?.getString("mobileNum") ?: ""

            TermsAgreementScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = {
                    navController.navigate(Route.MyProfile.withArgs(registerToken, mobileNum)) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}