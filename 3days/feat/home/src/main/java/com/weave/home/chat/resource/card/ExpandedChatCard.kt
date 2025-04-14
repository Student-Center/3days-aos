package com.weave.home.chat.resource.card

import StompCardColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.model.domain.chat.MessageContent

@Composable
fun ExpandedChatCard(
    content: MessageContent,
    isMyMessage: Boolean,
    onDismiss: () -> Unit
) {
    val stompCardColor = StompCardColor.entries.find { it.name == content.cardColor?.name } ?: StompCardColor.BLUE
    val bgColors = if (stompCardColor == StompCardColor.BLUE) {
        listOf(Color(0x00DAE6F1), Color(0xFFDAE6F1))
    } else {
        listOf(Color(0x00F3DDE5), Color(0xFFF3DDE5))
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DaysBackgroundTextureImage()
        Header(isMyMessage, onDismiss)

        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(0.8f)
                    .background(
                        brush = Brush.verticalGradient(bgColors)
                    )
            )
        }

        BigCard(
            content = content,
            stompCardColor = stompCardColor,
            isMyMessage = isMyMessage
        )
    }
}

@Composable
private fun Header(
    isMyMessage: Boolean,
    onDismiss: () -> Unit
) {
    val textContent = if (isMyMessage) "내가 보낸 카드" else "상대가 보낸 카드"

    Box(
        modifier = Modifier.statusBarsPadding()
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
                    tint = DaysTheme.colors.grey400
                )
            }
        }

        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textContent,
                style = DaysTheme.typography.semiBold18.toTextStyle(),
                color = DaysTheme.colors.grey500
            )
        }
    }
}

@Composable
private fun BigCard(
    content: MessageContent,
    stompCardColor: StompCardColor,
    isMyMessage: Boolean
) {
    val cardRes = if (stompCardColor == StompCardColor.BLUE) {
        com.weave.design_system.R.drawable.bg_chat_card_blue
    } else {
        com.weave.design_system.R.drawable.bg_chat_card_pink
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
        ) {

            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(cardRes),
                contentDescription = ""
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = 26.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = content.title ?: "",
                    style = DaysTheme.typography.semiBold14.toTextStyle(),
                    color = DaysTheme.colors.grey500
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = content.text?.take(150) ?: "",
                    style = DaysTheme.typography.regular15.copy(
                        fontSize = 20.dp, lineHeight = 30.dp
                    ).toTextStyle(),
                    color = DaysTheme.colors.grey500
                )
            }
        }

        if (isMyMessage) {
            Text(
                text = "이미 보낸 카드는 수정할 수 없어요",
                style = DaysTheme.typography.regular12.toTextStyle(),
                color = DaysTheme.colors.grey300
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
private fun ExpandedChatCardPreview() {
    ExpandedChatCard(
        content = MessageContent(
            type = MessageContent.Type.CARD,
            cardColor = MessageContent.CardColor.BLUE,
            text = "예시 텍스트를 작성합니다",
            title = "타이틀 입니다"
        ),
        isMyMessage = true,
        onDismiss = {}
    )
}