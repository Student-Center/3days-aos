package com.weave.home.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable

@Composable
fun BlankWidgetItem(
    onClick: () -> Unit,
    isSingle: Boolean = false,
    modifier: Modifier = Modifier
) {
    val stroke = Stroke(
        width = with(LocalDensity.current) { 2.dp.toPx() },
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
    )

    Box(
        modifier = if (isSingle) {
            modifier
                .height(156.dp)
                .fillMaxWidth()
        } else {
            modifier.aspectRatio(1f)
        }
            .applyShadow(
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            )
            .padding(6.dp)
            .noRippleClickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    shape = RoundedCornerShape(17.dp),
                    color = DaysTheme.colors.grey50
                )
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(1.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFE0DEDD),
                            style = stroke,
                            cornerRadius = CornerRadius(14.dp.toPx())
                        )
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Sharp.Add,
                    contentDescription = "",
                    tint = DaysTheme.colors.grey200,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "프로필 위젯\n추가하기",
                    style = DaysTheme.typography.semiBold14.toTextStyle(),
                    color = DaysTheme.colors.grey300,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlankWidgetItemPreview() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            BlankWidgetItem(
                onClick = {},
                isSingle = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlankWidgetItemPreview2() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BlankWidgetItem(
            onClick = {},
            modifier = Modifier.weight(1f)
        )
        BlankWidgetItem(
            onClick = {},
            modifier = Modifier.weight(1f)
        )
    }
}