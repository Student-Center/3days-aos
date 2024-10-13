package com.weave.design_system.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
fun DaysOnlyBackAppbar(
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp),
        title = { Text(text = "") },
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
                    contentDescription = "Back"
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
fun DaysOnlyBackAppbarPreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DaysOnlyBackAppbar(
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
