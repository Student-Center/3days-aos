package com.weave.a3days

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weave.intro.navGraphIntro
import com.weave.my_profile.navGraphMyProfile

enum class Route(val routeName: String) {
    Splash("splash"),
    Home("home"),
    Intro("intro"),
    MyProfile("my_profile");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

@Composable
fun DaysNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Route.Splash.routeName) {
        composable(Route.Splash.routeName) {
            SplashScreen(
                onDataLoadedResult = {
                    navController.navigate(
                        if (it) Route.Home.routeName else Route.Intro.routeName
                    ) {
                        popUpTo(Route.Splash.routeName) { inclusive = true }
                    }
                }
            )
        }
        navGraphIntro(navController)
        navGraphMyProfile(navController)
    }
}