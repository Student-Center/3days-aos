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
    NextScreen("next_screen");

    fun withArgs(vararg args: String): String {
        return buildString {
            append(routeName)
            args.forEach { arg -> append("/$arg") }
        }
    }
}

// 상수 정의
private const val REGISTER_TOKEN_ARG = "{registerToken}"

fun NavGraphBuilder.navGraphMyProfile(navController: NavController) {
    navigation(
        startDestination = Route.MyProfileInit.withArgs(REGISTER_TOKEN_ARG),
        route = Route.MyProfile.withArgs(REGISTER_TOKEN_ARG)
    ) {
        composable(
            route = Route.MyProfileInit.withArgs(REGISTER_TOKEN_ARG),
            arguments = listOf(navArgument("registerToken") {
                type = NavType.StringType; defaultValue = ""
            })
        ) { backStackEntry ->
            val registerToken = backStackEntry.arguments?.getString("registerToken") ?: ""

            MyProfileInitScreen(
                onNextBtnClicked = {
                    navController.navigateWithClearBackStack(
                        Route.MyProfileGender.withArgs(registerToken),
                        Route.MyProfile.withArgs(registerToken)
                    )
                },
            )
        }

        composable(
            route = Route.MyProfileGender.withArgs(REGISTER_TOKEN_ARG),
            arguments = listOf(navArgument("registerToken") {
                type = NavType.StringType; defaultValue = ""
            })
        ) { backStackEntry ->
            val registerToken = backStackEntry.arguments?.getString("registerToken") ?: ""
            val sharedViewModel =
                backStackEntry.sharedViewModel<MyProfileSharedViewModel>(navController = navController)
            sharedViewModel.registerToken = registerToken

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
                    // 개발 편의를 위한 주석 처리
//                    navController.navigateWithClearBackStack(
//                        Route.MyProfileBirth.routeName,
//                        Route.MyProfileInit.withArgs(registerToken)
//                    )
                    navController.navigateWithClearBackStack(
                        Route.PartnerInit.routeName,
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
                        Route.NextScreen.routeName,
                        Route.PartnerInit.routeName
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
