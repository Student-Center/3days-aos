package com.weave.a3days

import android.util.Log
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
        return args.joinToString(prefix = "$routeName/", separator = "/")
    }
}

@Composable
fun DaysNavGraph(navController: NavHostController) {
    navController.addOnDestinationChangedListener { controller, destination, _ ->
        val currentBackStack = controller.currentBackStack.value
        val routes = currentBackStack.mapNotNull { it.destination.route }.joinToString(", ")
        Log.d(
            "BackStackLog",
            "BackStack: $routes${if (routes.isNotBlank()) ", ${destination.route}" else ""}"
        )
    }

    NavHost(navController, startDestination = Route.Splash.routeName) {
        composable(Route.Splash.routeName) {
            SplashScreen { isDataLoaded ->
                navController.navigate(
                    if (isDataLoaded) Route.Home.routeName else Route.Intro.routeName
                ) {
                    popUpTo(Route.Splash.routeName) { inclusive = true }
                }
            }
        }
        navGraphIntro(navController)
        navGraphMyProfile(navController)
    }
}
