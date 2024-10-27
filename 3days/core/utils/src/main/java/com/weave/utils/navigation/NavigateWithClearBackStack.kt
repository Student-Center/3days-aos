package com.weave.utils.navigation

import androidx.navigation.NavController

fun NavController.navigateWithClearBackStack(
    destination: String,
    popUpToRoute: String,
    inclusive: Boolean = false
) {
    /**
     * @param destination 이동할 대상 라우트
     * @param popUpToRoute 백 스택에서 제거할 기준 라우트
     * @param inclusive popUpToRoute를 백 스택에서 제거할지 여부
     */
    this.navigate(destination) {
        popUpTo(popUpToRoute) { this.inclusive = inclusive }
    }
}