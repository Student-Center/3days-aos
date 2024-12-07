package com.weave.home.profile.main

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.noRippleClickable

private val DimColor = Color.Black.copy(alpha = 0.3f)
private val BorderColor = Color(0x80808080)
private val BlueTextColor = Color(0xFF007AFF)
private val GrayTextColor = Color(0xFF7F7F7F)

@Composable
fun ImageOptionDialog(
    onClickChange: () -> Unit,
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    val activity = LocalContext.current as? ComponentActivity

    activity?.let {
        DisposableEffect(Unit) {
            val window = it.window
            val originalColor = window.statusBarColor
            val insetsController = WindowInsetsControllerCompat(window, window.decorView)
            val originalLightStatusBar = insetsController.isAppearanceLightStatusBars

            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = DimColor.toArgb()
            insetsController.isAppearanceLightStatusBars = false

            onDispose {
                window.statusBarColor = originalColor
                insetsController.isAppearanceLightStatusBars = originalLightStatusBar
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        ImageOptionDialogContent(
            onClickChange = onClickChange,
            onClickDelete = onClickDelete,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun ImageOptionDialogContent(
    onClickChange: () -> Unit,
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DimColor)
            .noRippleClickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 36.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(13.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "프로필 사진 설정",
                    style = DaysTheme.typography.regular12.toTextStyle(),
                    color = GrayTextColor,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Divider()

                OptionText(
                    text = "앨범에서 사진 선택",
                    onClick = onClickChange
                )

                Divider()

                OptionText(
                    text = "기본 이미지 적용",
                    onClick = onClickDelete
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "취소",
                style = DaysTheme.typography.medium16.toTextStyle(),
                color = BlueTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(13.dp)
                    )
                    .noRippleClickable { onDismiss() }
                    .padding(vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun Divider() {
    HorizontalDivider(
        color = BorderColor,
        thickness = 0.6.dp
    )
}

@Composable
private fun OptionText(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = DaysTheme.typography.regular15.toTextStyle(),
        color = BlueTextColor,
        modifier = Modifier
            .noRippleClickable { onClick() }
            .padding(vertical = 14.dp)
    )
}

@Preview
@Composable
private fun ImageOptionDialogPreview() {
    DaysTheme {
        ImageOptionDialog(
            onClickChange = {},
            onClickDelete = {},
            onDismiss = {}
        )
    }
}