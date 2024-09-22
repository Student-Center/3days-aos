package com.weave.design_system.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow
import kotlinx.coroutines.launch

enum class SnackBarType {
    DEFAULT, ERROR
}

@Composable
fun DaysSnackBar(
    message: String,
    type: SnackBarType = SnackBarType.DEFAULT
) {
    val flag = type == SnackBarType.DEFAULT

    val (backgroundColor, textColor, borderColor) = if (flag) {
        Triple(
            DaysTheme.colors.white,
            DaysTheme.colors.grey400,
            DaysTheme.colors.grey50,
        )
    } else {
        Triple(
            Color(0xFFFFF5F8),
            DaysTheme.colors.red300,
            Color(0x80F2597F),
        )
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .size(320.dp, 52.dp)
            .applyShadow(
                RoundedCornerShape(14.dp),
                if (!flag) DaysTheme.shadow.alert else DaysTheme.shadow.default
            ),
        border = BorderStroke(1.dp, borderColor),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!flag) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = "Info Icon",
                        tint = DaysTheme.colors.red300,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp, 20.dp)
                    )
                }
                Text(
                    text = message,
                    style = DaysTheme.typography.medium16.toTextStyle(),
                    color = textColor,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview
@Composable
fun DaysSnackBarPreview() {
    val snackState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 20.dp),
                hostState = snackState,
            ) { snackData ->
                DaysSnackBar(
                    message = snackData.visuals.message,
                    type = SnackBarType.ERROR
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        snackState.showSnackbar("Custom Snackbar")
                    }
                },
            ) {
                Text("Show Snackbar")
            }
        }
    }
}