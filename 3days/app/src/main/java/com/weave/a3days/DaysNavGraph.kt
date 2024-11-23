package com.weave.a3days

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weave.home.navGraphHome
import com.weave.intro.navGraphIntro
import com.weave.my_profile.navGraphMyProfile
import com.weave.utils.LoggerUtil

enum class Route(val routeName: String) {
    Splash("splash"),
    Main("main"),
    Intro("intro");

    fun withArgs(vararg args: String): String {
        return args.joinToString(prefix = "$routeName/", separator = "/")
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun DaysNavGraph(navController: NavHostController) {
    navController.addOnDestinationChangedListener { controller, destination, _ ->
        val currentBackStack = controller.currentBackStack.value
        val routes = currentBackStack.mapNotNull { it.destination.route }.joinToString(", ")

        LoggerUtil.info(
            "[BackStack] $routes${if (routes.isNotBlank()) ", ${destination.route}" else ""}"
        )
    }

    NavHost(navController, startDestination = Route.Splash.routeName) {
        composable(Route.Splash.routeName) {
            SplashScreen { isDataLoaded ->
                navController.navigate(
                    if (isDataLoaded) Route.Main.routeName else Route.Intro.routeName
                ) {
                    popUpTo(Route.Splash.routeName) { inclusive = true }
                }
            }
        }
        navGraphIntro(navController)
        navGraphMyProfile(navController)
        navGraphHome(navController)
    }
}
