package com.weave.design_system.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.extension.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysEditTopBar(
    title: String = "",
    onBackPressed: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        title = {
            if (title.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = DaysTheme.typography.semiBold18.toTextStyle(),
                        color = DaysTheme.colors.grey500,
                        modifier = Modifier
                    )
                }
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .size(48.dp, 48.dp)
                    .noRippleClickable(onBackPressed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp, 24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_arrow_back),
                    contentDescription = "Back",
                    tint = DaysTheme.colors.grey400
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = DaysTheme.colors.bgDefault,
            navigationIconContentColor = DaysTheme.colors.grey500
        )
    )
}

@Preview
@Composable
fun DaysEditTopBarPreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysEditTopBar(
                onBackPressed = {}
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}