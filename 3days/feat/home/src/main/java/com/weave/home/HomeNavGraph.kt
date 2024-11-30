package com.weave.home

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weave.home.profile.ProfileEditType
import com.weave.home.profile.UserInfo
import com.weave.home.profile.UserInfoType
import com.weave.home.profile.company.EditCompanyScreen
import com.weave.home.profile.job.EditJobScreen
import com.weave.home.profile.location.EditLocationScreen
import com.weave.home.profile.main.SnackBarViewModel
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.UserDesiredPartner
import com.weave.utils.navigation.navigateWithClearBackStack

private const val USER_INFO_KEY = "user_info"
private val gson = Gson()
private val listType = object : TypeToken<UserInfo>() {}.type

enum class Route(val routeName: String) {
    Main("main"),
    Home("home"),
    Profile("profile"),
    ProfileEditJob("edit_job/{$USER_INFO_KEY}"),
    ProfileEditCompany("edit_company/{$USER_INFO_KEY}"),
    ProfileEditLocation("edit_location/{$USER_INFO_KEY}");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg -> append("/$arg") }
        }
    }

    fun createEditProfileRoute(userInfo: UserInfo?): String {
        userInfo ?: return routeName

        return routeName.replace(
            "{$USER_INFO_KEY}",
            Uri.encode(gson.toJson(userInfo))
        )
    }
}

fun NavGraphBuilder.navGraphHome(navController: NavController) {
    navigation(startDestination = Route.Home.withArgs("0"), route = Route.Main.routeName) {
        composable(
            route = Route.Home.withArgs("{targetScreen}"),
            arguments = listOf(
                navArgument("targetScreen") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val targetScreen =
                backStackEntry.arguments?.getString("targetScreen")?.toIntOrNull() ?: 0
            val snackBarViewModel = backStackEntry.sharedViewModel<SnackBarViewModel>(navController)

            MainTabScreen(
                snackBarViewModel = snackBarViewModel,
                targetScreen = if (targetScreen == 0) TabType.HOME else TabType.PROFILE,
                moveToMyProfileEdit = { type, item ->
                    val destination = when (type) {
                        ProfileEditType.JOB_OCCUPATION ->
                            Route.ProfileEditJob.createEditProfileRoute(item)

                        ProfileEditType.COMPANY -> {
                            Route.ProfileEditCompany.createEditProfileRoute(item)
                        }

                        ProfileEditType.LOCATION -> {
                            Route.ProfileEditLocation.createEditProfileRoute(item)
                        }

                        ProfileEditType.PARTNER_AGE -> {Route.ProfileEditJob.createEditProfileRoute(item)}
                        ProfileEditType.PARTNER_JOB_OCCUPATION -> {Route.ProfileEditJob.createEditProfileRoute(item)}
                        ProfileEditType.PARTNER_DISTANCE -> {Route.ProfileEditJob.createEditProfileRoute(item)}
                    }

                    navController.navigateWithClearBackStack(
                        destination = destination,
                        popUpToRoute = Route.Main.routeName
                    )
                }
            )
        }

        composable(
            route = Route.ProfileEditJob.routeName,
            arguments = listOf(
                navArgument(USER_INFO_KEY) { type = UserInfoType() }
            )
        ) { backStackEntry ->
            val snackBarViewModel = backStackEntry.sharedViewModel<SnackBarViewModel>(navController)

            EditJobScreen(
                snackBarViewModel = snackBarViewModel,
                userInfo = parseMyInfoDisplay(backStackEntry),
                navigateToProfile = {
                    navController.navigateWithClearBackStack(
                        Route.Home.withArgs("1"),
                        Route.ProfileEditJob.routeName,
                    )
                }
            )
        }

        composable(
            route = Route.ProfileEditCompany.routeName,
            arguments = listOf(
                navArgument(USER_INFO_KEY) { type = UserInfoType() }
            )
        ) { backStackEntry ->
            val snackBarViewModel = backStackEntry.sharedViewModel<SnackBarViewModel>(navController)

            EditCompanyScreen(
                snackBarViewModel = snackBarViewModel,
                userInfo = parseMyInfoDisplay(backStackEntry),
                navigateToProfile = {
                    navController.navigateWithClearBackStack(
                        Route.Home.withArgs("1"),
                        Route.ProfileEditCompany.routeName,
                    )
                }
            )
        }

        composable(
            route = Route.ProfileEditLocation.routeName,
            arguments = listOf(
                navArgument(USER_INFO_KEY) { type = UserInfoType() }
            )
        ) { backStackEntry ->
            val snackBarViewModel = backStackEntry.sharedViewModel<SnackBarViewModel>(navController)

            EditLocationScreen(
                snackBarViewModel = snackBarViewModel,
                userInfo = parseMyInfoDisplay(backStackEntry),
                navigateToProfile = {
                    navController.navigateWithClearBackStack(
                        Route.Home.withArgs("1"),
                        Route.ProfileEditLocation.routeName,
                    )
                }
            )
        }
    }
}

private fun parseMyInfoDisplay(backStackEntry: NavBackStackEntry): UserInfo {
    val defaultValue = UserInfo(
        name = "",
        jobOccupation = JobOccupation.OTHER,
        locations = emptyList(),
        desiredPartner = null
    )

    return try {
        backStackEntry.arguments?.getString(USER_INFO_KEY)?.let { jsonString ->
            gson.fromJson(jsonString, listType)
        } ?: defaultValue
    } catch (e: Exception) {
        defaultValue
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