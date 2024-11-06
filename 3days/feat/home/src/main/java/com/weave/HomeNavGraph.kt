package com.weave

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.weave.home.complete.CompleteRegisterScreen

enum class Route(val routeName: String) {
    Home("home"),
    HomeMain("home_main");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg -> append("/$arg") }
        }
    }
}

fun NavGraphBuilder.navGraphHome(navController: NavController) {
    navigation(startDestination = Route.HomeMain.routeName, route = Route.Home.routeName) {
        composable(Route.HomeMain.routeName) {
            CompleteRegisterScreen()
        }
    }
}