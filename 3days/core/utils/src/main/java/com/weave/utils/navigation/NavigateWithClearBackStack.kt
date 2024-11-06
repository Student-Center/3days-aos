package com.weave.utils.navigation

import androidx.navigation.NavController
import com.weave.utils.LoggerUtil

fun NavController.navigateWithClearBackStack(
    destination: String,
    popUpToRoute: String,
    inclusive: Boolean = true,
    launchSingleTop: Boolean = false,
) {
    /**
     * @param destination 이동할 대상 라우트
     * @param popUpToRoute 백 스택에서 제거할 기준 라우트
     * @param inclusive popUpToRoute를 백 스택에서 제거할지 여부
     */
    LoggerUtil.info(
        "[BackStackLog] ${destination}로 이동, ${popUpToRoute}제거 (inclusive: $inclusive)"
    )
    this.navigate(destination) {
        popUpTo(popUpToRoute) { this.inclusive = inclusive }
        this.launchSingleTop = launchSingleTop
    }
}