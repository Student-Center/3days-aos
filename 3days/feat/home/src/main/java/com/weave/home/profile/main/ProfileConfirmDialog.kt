package com.weave.home.profile.main

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import coil.compose.rememberAsyncImagePainter
import com.weave.design_system.DaysTheme
import java.io.File

@Composable
fun ProfileConfirmDialog(
    imageFile: File,
    onConfirm: (File) -> Unit,
    onDismiss: () -> Unit
) {
    val activity = LocalContext.current as? ComponentActivity

    activity?.let {
        DisposableEffect(Unit) {
            val window = it.window
            val originalColor = window.statusBarColor
            val insetsController = WindowInsetsControllerCompat(window, window.decorView)
            val originalLightStatusBar = insetsController.isAppearanceLightStatusBars

            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.Black.toArgb()
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
            dismissOnClickOutside = false,
        )
    ) {
        Box(modifier = Modifier.background(Color.Black))
        ProfileConfirmView(
            imageFile = imageFile,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun ProfileConfirmView(
    imageFile: File,
    onConfirm: (File) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = DaysTheme.colors.white
                    )
                }

                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "내 프로필 설정",
                        style = DaysTheme.typography.semiBold18.toTextStyle(),
                        color = DaysTheme.colors.white,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(imageFile)
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 90.dp)
                        .aspectRatio(1f),
                    painter = painter,
                    contentDescription = "Profile Image"
                )
            }
        }

        Button(
            onClick = { onConfirm(imageFile) },
            enabled = true,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    brush = Brush.horizontalGradient(DaysTheme.colors.gradientA),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            Text(
                text = "프로필 사진으로 등록하기",
                style = DaysTheme.typography.semiBold18.toTextStyle(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun ProfileConfirmDialogPreview() {
    ProfileConfirmDialog(
        imageFile = File(""),
        onConfirm = {},
        onDismiss = {}
    )
}