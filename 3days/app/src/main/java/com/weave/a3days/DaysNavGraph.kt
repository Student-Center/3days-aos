package com.weave.a3days

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.weave.intro.navGraphIntro

@Composable
fun DaysNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "intro") {
        navGraphIntro(navController)
    }
}