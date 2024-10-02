package com.weave.my_profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.navGraphMyProfile(navController: NavController) {
    navigation(startDestination = "my_profile_init", route = "my_profile") {
        composable("my_profile_init") {
            MyProfileInitScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { navController.navigate("my_profile_gender") },
            )
        }
        composable("my_profile_gender") {
            MyProfileGenderScreen(
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { navController.navigate("my_profile_age") }
            )
        }
    }
}