package com.weave

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.weave.home.MainTabScreen

enum class Route(val routeName: String) {
    Main("main"),
    Home("home"),
    Profile("profile");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg -> append("/$arg") }
        }
    }
}

fun NavGraphBuilder.navGraphHome(navController: NavController) {
    navigation(startDestination = Route.Home.routeName, route = Route.Main.routeName) {
        composable(Route.Home.routeName) {
            MainTabScreen()
        }
    }
}