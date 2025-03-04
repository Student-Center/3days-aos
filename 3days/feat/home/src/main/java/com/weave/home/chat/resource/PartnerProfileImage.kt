package com.weave.home.chat.resource

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow
import com.weave.home.R

@Composable
fun PartnerProfileImage(
    profileImage: String
) {
    Box(
        modifier = Modifier
            .size(32.dp, 32.dp)
            .clip(CircleShape)
            .border(
                color = DaysTheme.colors.white,
                width = 2.4.dp,
                shape = CircleShape
            )
    ) {
        SubcomposeAsyncImage(
            model = profileImage,
            contentDescription = "",
            loading = { DefaultProfileImage() },
            error = { DefaultProfileImage() },
            success = { state ->
                Image(
                    painter = state.painter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(102.dp)
                        .applyShadow(
                            shape = RoundedCornerShape(42.95.dp)
                        )
                        .clip(RoundedCornerShape(42.95.dp))
                )
            }
        )
    }
}

@Composable
private fun DefaultProfileImage() {
    Image(
        painter = painterResource(R.drawable.png_profile_bg),
        contentDescription = ""
    )
}