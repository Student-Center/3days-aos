package com.weave

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
import com.weave.home.MainTabScreen
import com.weave.home.TabType
import com.weave.home.profile.ProfileEditType
import com.weave.home.profile.SnackBarViewModel
import com.weave.home.profile.company.EditCompanyScreen
import com.weave.home.profile.job.EditJobScreen
import com.weave.home.profile.location.EditLocationScreen
import com.weave.home.profile.location.LocationListType
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.utils.LoggerUtil
import com.weave.utils.navigation.navigateWithClearBackStack
import java.util.UUID

private const val LOCATIONS_KEY = "locations"
private val gson = Gson()
private val listType = object : TypeToken<List<Pair<UUID, String>>>() {}.type

enum class Route(val routeName: String) {
    Main("main"),
    Home("home"),
    Profile("profile"),
    ProfileEditJob("edit_job"),
    ProfileEditCompany("edit_company"),
    ProfileEditLocation("edit_location/{$LOCATIONS_KEY}");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg -> append("/$arg") }
        }
    }

    fun createLocationRoute(locations: List<Pair<UUID, String>>): String {
        return routeName.replace(
            "{$LOCATIONS_KEY}",
            Uri.encode(gson.toJson(locations))
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
                            Route.ProfileEditJob.withArgs((item as JobOccupation).koValue)

                        ProfileEditType.COMPANY -> {
                            val company = if (item != null) item as Company else null
                            Route.ProfileEditCompany.withArgs(
                                company?.id?.toString() ?: "",
                                company?.name ?: ""
                            )
                        }

                        ProfileEditType.LOCATION -> {
                            val locations = item as? List<Pair<UUID, String>> ?: emptyList()
                            Route.ProfileEditLocation.createLocationRoute(locations)
                        }
                    }

                    navController.navigateWithClearBackStack(
                        destination = destination,
                        popUpToRoute = Route.Main.routeName
                    )
                }
            )
        }

        composable(
            route = Route.ProfileEditJob.withArgs("{occupation}"),
            arguments = listOf(
                navArgument("occupation") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val occupation = backStackEntry.arguments?.getString("occupation")?.let {
                JobOccupation.findFromKoValue(it)
            } ?: JobOccupation.OTHER

            val snackBarViewModel = backStackEntry.sharedViewModel<SnackBarViewModel>(navController)

            EditJobScreen(
                snackBarViewModel = snackBarViewModel,
                initOccupation = occupation,
                navigateToProfile = {
                    navController.navigateWithClearBackStack(
                        Route.Home.withArgs("1"),
                        Route.ProfileEditJob.routeName,
                    )
                }
            )
        }

        composable(
            route = Route.ProfileEditCompany.withArgs("{uuid}", "{name}"),
            arguments = listOf(
                navArgument("uuid") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val snackBarViewModel = backStackEntry.sharedViewModel<SnackBarViewModel>(navController)

            val name = backStackEntry.arguments?.getString("name")?.let {
                it.ifBlank { null }
            }
            val uuid = backStackEntry.arguments?.getString("uuid")?.let {
                if (it.isNotBlank()) UUID.fromString(it) else null
            }

            val company = if (name == null || uuid == null) null else Company(uuid, name)

            EditCompanyScreen(
                snackBarViewModel = snackBarViewModel,
                initCompany = company,
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
                navArgument(LOCATIONS_KEY) { type = LocationListType() }
            )
        ) { backStackEntry ->
            val snackBarViewModel = backStackEntry.sharedViewModel<SnackBarViewModel>(navController)
            val locations = try {
                backStackEntry.arguments?.getString(LOCATIONS_KEY)?.let { jsonString ->
                    gson.fromJson<List<Pair<String, String>>>(jsonString, listType)
                        .map { (first, second) ->
                            Pair(UUID.fromString(first), second)
                        }
                } ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }

            LoggerUtil.info(locations.toString())

            EditLocationScreen(
                snackBarViewModel = snackBarViewModel,
                initLocations = locations,
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

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}