package com.weave.my_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import kotlinx.coroutines.delay

@Composable
fun MyProfileInitScreen(
    onNextBtnClicked: () -> Unit
) {
    val screenHeight =
        LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.heightPixels.toDp() }

    LaunchedEffect(Unit) {
        delay(3000)
        onNextBtnClicked()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DaysTheme.colors.bgDefault),
                painter = painterResource(id = R.drawable.texture_bg),
                contentDescription = stringResource(id = R.string.background_description)
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0x00F3DDE5), Color(0xFFF3DDE5))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.ic_reading_glasses),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.my_profile_init_title),
                    style = DaysTheme.typography.semiBold20.toTextStyle(),
                    color = DaysTheme.colors.grey500,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun MyProfileInitScreenPreview() {
    MyProfileInitScreen(
        onNextBtnClicked = {}
    )
}