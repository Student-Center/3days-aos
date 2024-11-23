package com.weave.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.home.HomeScreen
import com.weave.home.profile.ProfileEditType
import com.weave.home.profile.ProfileScreen
import com.weave.home.profile.SnackBarViewModel

enum class TabType {
    HOME, PROFILE
}

@Composable
fun MainTabScreen(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    targetScreen: TabType = TabType.HOME,
    moveToMyProfileEdit: (ProfileEditType, Any) -> Unit
) {
    var selectedTab by remember { mutableStateOf(targetScreen) }

    MainTabScreenContent(
        snackState = snackBarViewModel.snackBarHostState,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        moveToMyProfileEdit = moveToMyProfileEdit
    )
}

@Composable
private fun MainTabScreenContent(
    snackState: SnackbarHostState,
    selectedTab: TabType,
    onTabSelected: (TabType) -> Unit,
    modifier: Modifier = Modifier,
    moveToMyProfileEdit: (ProfileEditType, Any) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            DaysSnackBarHost(
                snackState = snackState,
                modifier = Modifier.padding(bottom = 110.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DaysBackgroundTextureImage()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
            ) {
                TabRow(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )

                Spacer(modifier = Modifier.height(4.dp))

                TabContent(
                    selectedTab = selectedTab,
                    moveToMyProfileEdit = moveToMyProfileEdit
                )
            }
        }
    }
}

@Composable
private fun TabRow(
    selectedTab: TabType,
    onTabSelected: (TabType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(
            text = "HOME",
            isSelected = selectedTab == TabType.HOME,
            onClick = { onTabSelected(TabType.HOME) }
        )

        Spacer(modifier = Modifier.width(30.dp))

        TabItem(
            text = "PROFILE",
            isSelected = selectedTab == TabType.PROFILE,
            onClick = { onTabSelected(TabType.PROFILE) }
        )
    }
}

@Composable
private fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.noRippleClickable(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TabIndicator(isSelected = isSelected)

        Text(
            text = text,
            style = DaysTheme.typography.enMedium20.toTextStyle(),
            color = if (isSelected) DaysTheme.colors.grey500 else Color(0x33534C44)
        )
    }
}

@Composable
private fun TabIndicator(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(6.dp)
            .background(
                color = if (isSelected) DaysTheme.colors.grey500 else Color.Transparent,
                shape = CircleShape
            )
    )
}

@Composable
private fun TabContent(
    selectedTab: TabType,
    moveToMyProfileEdit: (ProfileEditType, Any) -> Unit
) {
    when (selectedTab) {
        TabType.HOME -> HomeScreen()
        TabType.PROFILE -> ProfileScreen(
            moveToMyProfileEdit = { type, item ->
                moveToMyProfileEdit(type, item)
            }
        )
    }
}

@Preview
@Composable
private fun MainTabScreenPreview() {
    val snackState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableStateOf(TabType.HOME) }

    MainTabScreenContent(
        snackState = snackState,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        moveToMyProfileEdit = { type, item -> }
    )
}