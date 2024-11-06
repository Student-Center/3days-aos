package com.weave.my_profile

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
import com.weave.my_profile.birth.MyProfileBirthYearScreen
import com.weave.my_profile.company.MyProfileCompanyScreen
import com.weave.my_profile.gender.MyProfileGenderScreen
import com.weave.my_profile.init.MyProfileInitScreen
import com.weave.my_profile.location.MyProfileLocationScreen
import com.weave.my_profile.nick.MyProfileNickScreen
import com.weave.my_profile.occupation.MyProfileOccupationScreen
import com.weave.my_profile.partner.PartnerInitScreen
import com.weave.my_profile.partner.age.PartnerAgeScreen
import com.weave.my_profile.partner.distance.PartnerDistanceScreen
import com.weave.my_profile.partner.occupation.PartnerOccupationScreen
import com.weave.utils.navigation.navigateWithClearBackStack

enum class Route(val routeName: String) {
    MyProfile("my_profile"),
    MyProfileInit("my_profile_init"),
    MyProfileGender("my_profile_gender"),
    MyProfileBirth("my_profile_birth"),
    MyProfileCompany("my_profile_company"),
    MyProfileOccupation("my_profile_occupation"),
    MyProfileLocation("my_profile_location"),
    MyProfileNickName("my_profile_nick"),
    PartnerInit("partner_init"),
    PartnerAge("partner_age"),
    PartnerOccupation("partner_occupation"),
    PartnerDistance("partner_distance"),
    Home("home");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg -> append("/$arg") }
        }
    }
}

private const val REGISTER_TOKEN_ARG = "{registerToken}"
private const val MOBILE_NUM_ARG = "{mobileNum}"

fun NavGraphBuilder.navGraphMyProfile(navController: NavController) {
    navigation(
        startDestination = Route.MyProfileInit.withArgs(REGISTER_TOKEN_ARG, MOBILE_NUM_ARG),
        route = Route.MyProfile.withArgs(REGISTER_TOKEN_ARG, MOBILE_NUM_ARG)
    ) {
        composable(
            route = Route.MyProfileInit.withArgs(REGISTER_TOKEN_ARG, MOBILE_NUM_ARG),
            arguments = listOf(
                navArgument("registerToken") { type = NavType.StringType },
                navArgument("mobileNum") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val registerToken = backStackEntry.arguments?.getString("registerToken") ?: ""
            val mobileNum = backStackEntry.arguments?.getString("mobileNum") ?: ""

            MyProfileInitScreen(
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileGender.withArgs(registerToken, mobileNum),
                        Route.MyProfile.withArgs(registerToken)
                    )
                },
            )
        }

        composable(
            route = Route.MyProfileGender.withArgs(REGISTER_TOKEN_ARG, MOBILE_NUM_ARG),
            arguments = listOf(
                navArgument("registerToken") { type = NavType.StringType },
                navArgument("mobileNum") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val registerToken = backStackEntry.arguments?.getString("registerToken") ?: ""
            val mobileNum = backStackEntry.arguments?.getString("mobileNum") ?: ""

            val sharedViewModel =
                backStackEntry.sharedViewModel<MyProfileSharedViewModel>(navController = navController)
            sharedViewModel.registerToken = registerToken
            sharedViewModel.phoneNumber = mobileNum

            MyProfileGenderScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileInit.withArgs(registerToken),
                        Route.MyProfileGender.withArgs(registerToken),
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileBirth.routeName,
                        Route.MyProfileInit.withArgs(registerToken)
                    )
                }
            )
        }

        composable(Route.MyProfileBirth.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            MyProfileBirthYearScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileGender.withArgs(sharedViewModel.registerToken),
                        Route.MyProfileBirth.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileCompany.routeName,
                        Route.MyProfileGender.withArgs(sharedViewModel.registerToken)
                    )
                }
            )
        }

        composable(Route.MyProfileCompany.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            MyProfileCompanyScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileBirth.routeName,
                        Route.MyProfileCompany.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileOccupation.routeName,
                        Route.MyProfileBirth.routeName
                    )
                }
            )
        }

        composable(Route.MyProfileOccupation.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            MyProfileOccupationScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileCompany.routeName,
                        Route.MyProfileOccupation.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileLocation.routeName,
                        Route.MyProfileCompany.routeName
                    )
                }
            )
        }

        composable(Route.MyProfileLocation.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            MyProfileLocationScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileOccupation.routeName,
                        Route.MyProfileLocation.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileNickName.routeName,
                        Route.MyProfileOccupation.routeName
                    )
                }
            )
        }

        composable(Route.MyProfileNickName.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            MyProfileNickScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileLocation.routeName,
                        Route.MyProfileNickName.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.PartnerInit.routeName,
                        Route.MyProfileLocation.routeName
                    )
                }
            )
        }

        composable(Route.PartnerInit.routeName) {
            PartnerInitScreen(
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileNickName.routeName,
                        Route.PartnerInit.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.PartnerAge.routeName,
                        Route.MyProfileNickName.routeName
                    )
                }
            )
        }

        composable(Route.PartnerAge.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            PartnerAgeScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.PartnerInit.routeName,
                        Route.PartnerAge.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.PartnerOccupation.routeName,
                        Route.PartnerInit.routeName
                    )
                }
            )
        }

        composable(Route.PartnerOccupation.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            PartnerOccupationScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.PartnerAge.routeName,
                        Route.PartnerOccupation.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.PartnerDistance.routeName,
                        Route.PartnerAge.routeName
                    )
                }
            )
        }

        composable(Route.PartnerDistance.routeName) {
            val sharedViewModel =
                it.sharedViewModel<MyProfileSharedViewModel>(navController = navController)

            PartnerDistanceScreen(
                sharedViewModel = sharedViewModel,
                onBackBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.PartnerOccupation.routeName,
                        Route.PartnerDistance.routeName,
                        launchSingleTop = true
                    )
                },
                onNextBtnClicked = {
                    navController.navigate(Route.Home.routeName) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
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
