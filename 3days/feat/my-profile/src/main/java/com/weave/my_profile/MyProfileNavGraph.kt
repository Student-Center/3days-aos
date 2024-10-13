package com.weave.my_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.navGraphMyProfile(navController: NavController) {
    navigation(startDestination = "my_profile_init", route = "my_profile") {
        composable("my_profile_init") {
            MyProfileInitScreen(
                onNextBtnClicked = { navController.navigate("my_profile_gender") },
            )
        }
        composable("my_profile_gender") {
            val sharedViewModel = it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            MyProfileGenderScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { navController.navigate("my_profile_birth") }
            )
        }
        composable("my_profile_birth") {
            val sharedViewModel = it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            MyProfileBirthYearScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = { navController.popBackStack() },
                onNextBtnClicked = { navController.navigate("next_screen") }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}