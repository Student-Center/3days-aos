package com.weave.home.chat.resource

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow

@Composable
fun ChatHeader(
    topPadding: Dp,
    profileImage: String,
    moveToHome: () -> Unit
) {
    val bottomShape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .zIndex(1f)
            .fillMaxWidth()
            .wrapContentHeight()
            .applyShadow(
                shape = bottomShape,
                shadowType = DaysTheme.shadow.default.copy(blur = 30.dp)
            )
            .border(
                BorderStroke(0.5.dp, color = Color(0x1A534C44)),
                shape = bottomShape
            )
            .background(
                color = DaysTheme.colors.white.copy(alpha = 0.95f),
                shape = bottomShape
            )
    ) {
        Spacer(modifier = Modifier.height(topPadding))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = moveToHome
            ) {
                Icon(
                    painter = painterResource(com.weave.design_system.R.drawable.ic_round_arrow_back),
                    contentDescription = "",
                    tint = DaysTheme.colors.grey400
                )
            }

            Column(modifier = Modifier) {
                PartnerProfileImage(profileImage)
            }
        }
    }
}