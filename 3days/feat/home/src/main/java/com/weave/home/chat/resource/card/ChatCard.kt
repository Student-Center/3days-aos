package com.weave.home.chat.resource.card

import CardColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.chat.resource.PartnerProfileImage
import com.weave.model.domain.chat.MessageContent

@Composable
fun ChatCard(
    content: MessageContent,
    isMyMessage: Boolean,
    profileImage: String,
    onCardClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        if (!isMyMessage) {
            PartnerProfileImage(profileImage)
            Spacer(modifier = Modifier.size(width = 8.dp, height = 1.dp))
        }

        ChatCardContent(
            cardColor = CardColor.entries.find { it.name == content.cardColor } ?: CardColor.BLUE,
            isMyMessage = isMyMessage,
            onCardClick = onCardClick
        )
    }
}

@Composable
private fun ChatCardContent(
    cardColor: CardColor,
    isMyMessage: Boolean,
    onCardClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    val cardRes =
        if (cardColor == CardColor.BLUE) {
            com.weave.design_system.R.drawable.bg_chat_card_blue
        } else {
            com.weave.design_system.R.drawable.bg_chat_card_pink
        }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .size(85.dp, 121.dp)
            .applyShadow(
                shape = shape,
                shadowType = DaysTheme.shadow.default.copy(
                    shadowColor = Color(0x1A4F4743),
                    offsetY = 0.71.dp
                )
            )
    ) {
        Image(
            modifier = Modifier.noRippleClickable { onCardClick() },
            painter = painterResource(cardRes),
            contentDescription = "",
        )

        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.Center
        ) {
            ChatCardButton(
                cardColor,
                isMyMessage,
                onCardClick
            )
        }
    }
}

@Composable
private fun ChatCardButton(
    cardColor: CardColor,
    isMyMessage: Boolean,
    onCardClick: () -> Unit
) {
    val buttonColors =
        if (cardColor == CardColor.BLUE) {
            DaysTheme.colors.blue500 to DaysTheme.colors.blue50
        } else {
            DaysTheme.colors.pink500 to DaysTheme.colors.pink50
        }

    IconButton(
        modifier = Modifier
            .background(
                shape = CircleShape,
                color = buttonColors.first
            )
            .size(30.dp),
        onClick = onCardClick
    ) {
        Icon(
            painter = painterResource(com.weave.design_system.R.drawable.ic_round_arrow_back),
            contentDescription = "",
            tint = buttonColors.second,
            modifier = Modifier.graphicsLayer(scaleX = if (isMyMessage) 1f else -1f)
        )
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
private fun ChatCardPreview() {

    Column {
        Spacer(Modifier.height(20.dp))
        ChatCard(
            isMyMessage = true,
            profileImage = "",
            content = MessageContent(
                cardColor = CardColor.BLUE.name,
                text = "This is Blue Card.",
                type = MessageContent.Type.CARD
            )
        ) {}

        Spacer(Modifier.height(20.dp))

        ChatCard(
            isMyMessage = false,
            profileImage = "",
            content = MessageContent(
                cardColor = CardColor.PINK.name,
                text = "This is Pink Card.",
                type = MessageContent.Type.CARD
            )
        ) {}
        Spacer(Modifier.height(20.dp))
    }
}