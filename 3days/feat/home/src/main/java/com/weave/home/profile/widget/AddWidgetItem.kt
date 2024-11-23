package com.weave.home.profile.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.R
import com.weave.model.domain.user.ProfileWidgetType

@Composable
fun AddWidgetItem(
    widgetType: ProfileWidgetType,
    alreadyDone: Boolean = false,
    onClick: (ProfileWidgetType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    widgetType.color.containerColor.map { Color(it) }
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.matchParentSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = widgetType.title,
                    style = DaysTheme.typography.semiBold20.copy(fontSize = 22.dp).toTextStyle(),
                    color = Color(widgetType.color.textColor)
                )

                if (!alreadyDone) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "",
                        tint = Color(0x4D000000),
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onClick(widgetType) }
                    )
                }
            }

            Text(
                text = widgetType.getExample(),
                style = DaysTheme.typography.regular14.toTextStyle(),
                color = Color(widgetType.color.textColor).copy(alpha = 0.6f)
            )
        }
    }

    if (alreadyDone) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0x73FFFFFF))
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                painter = painterResource(id = R.drawable.png_check),
                contentDescription = ""
            )
        }
    }
}

@Preview
@Composable
private fun AddWidgetItemPreview() {
    Column {
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(horizontal = 26.dp, vertical = 22.dp),
        ) {
            AddWidgetItem(
                widgetType = ProfileWidgetType.HOBBY,
                alreadyDone = true,
                onClick = {}
            )
        }

        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(horizontal = 26.dp, vertical = 22.dp),
        ) {
            AddWidgetItem(
                widgetType = ProfileWidgetType.HOBBY,
                alreadyDone = false,
                onClick = {}
            )
        }
    }
}