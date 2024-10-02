package com.weave.a3days

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weave.intro.navGraphIntro
import com.weave.my_profile.navGraphMyProfile

@Composable
fun DaysNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onDataLoadedResult = {
                    navController.navigate(
                        if (it) "home" else "intro"
                    )
                }
            )
        }
        navGraphIntro(navController)
        navGraphMyProfile(navController)
    }
}