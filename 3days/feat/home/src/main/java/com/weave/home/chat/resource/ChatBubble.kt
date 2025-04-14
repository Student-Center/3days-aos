package com.weave.home.chat.resource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.home.chat.generateDummyMessages
import com.weave.home.chat.resource.card.ChatCard
import com.weave.model.domain.chat.Message
import com.weave.model.domain.chat.MessageContent
import com.weave.utils.DateTimeUtil

@Composable
fun ChatBubble(
    message: Message,
    position: MessagePosition,
    isMyMessage: Boolean,
    isNewMin: Boolean,
    profileImage: String,
    onCardClick: (Message) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start,
    ) {
        when (message.content.type) {
            MessageContent.Type.TEXT ->
                TextMessageBubble(
                    message,
                    position,
                    isMyMessage,
                    profileImage,
                )

            MessageContent.Type.CARD ->
                CardMessageBubble(
                    message,
                    profileImage,
                    isMyMessage,
                    onCardClick,
                )

            MessageContent.Type.SYSTEM -> SystemMessageBubble(message)
            null -> {}
        }
    }
}

@Composable
fun TextMessageBubble(
    message: Message,
    position: MessagePosition,
    isMyMessage: Boolean,
    profileImage: String,
) {
    var isMultiLine by remember { mutableStateOf(false) }

    val myCorners =
        if (isMultiLine) listOf(4.dp, 20.dp, 20.dp, 20.dp) else listOf(4.dp, 100.dp, 100.dp, 20.dp)
    val otherCorners =
        if (isMultiLine) listOf(20.dp, 4.dp, 20.dp, 20.dp) else listOf(100.dp, 4.dp, 20.dp, 100.dp)

    val (corner1, corner2, corner3, corner4) = if (isMyMessage) myCorners else otherCorners

    val shape =
        when (position) {
            MessagePosition.FIRST ->
                RoundedCornerShape(
                    topStart = corner3,
                    topEnd = corner4,
                    bottomStart = corner2,
                    bottomEnd = corner1,
                )

            MessagePosition.MIDDLE ->
                RoundedCornerShape(
                    topStart = corner2,
                    topEnd = corner1,
                    bottomStart = corner2,
                    bottomEnd = corner1,
                )

            MessagePosition.LAST ->
                RoundedCornerShape(
                    topStart = if (isMyMessage) corner3 else 4.dp,
                    topEnd = if (isMyMessage) corner1 else 20.dp,
                    bottomStart = if (isMyMessage) corner2 else 20.dp,
                    bottomEnd = if (isMyMessage) corner4 else 20.dp,
                )

            MessagePosition.SINGLE -> RoundedCornerShape(20.dp)
        }

    Row(verticalAlignment = Alignment.Bottom) {
        if (!isMyMessage && (position == MessagePosition.SINGLE || position == MessagePosition.LAST)) {
            PartnerProfileImage(profileImage)
            Spacer(modifier = Modifier.size(width = 8.dp, height = 1.dp))
        }

        if (!isMyMessage && (position == MessagePosition.MIDDLE || position == MessagePosition.FIRST)) {
            Spacer(modifier = Modifier.size(width = 40.dp, height = 1.dp))
        }

        if (isMyMessage && (position == MessagePosition.SINGLE || position == MessagePosition.LAST)) {
            Text(
                style =
                    DaysTheme.typography.regular12
                        .copy(fontSize = 10.dp)
                        .toTextStyle(),
                text = DateTimeUtil.formatChatTime(message.createdAt),
                color = Color(0x80534C44),
            )
            Spacer(Modifier.width(6.dp))
        }

        Box(
            modifier =
                Modifier
                    .background(if (isMyMessage) DaysTheme.colors.blue300 else Color.White, shape)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 240.dp),
        ) {
            Text(
                style = DaysTheme.typography.regular15.toTextStyle(),
                text = message.content.text ?: "",
                color = if (isMyMessage) Color.White else DaysTheme.colors.grey500,
                onTextLayout = { textLayoutResult ->
                    isMultiLine = textLayoutResult.lineCount > 1
                },
            )
        }

        if (!isMyMessage && (position == MessagePosition.SINGLE || position == MessagePosition.LAST)) {
            Spacer(Modifier.width(6.dp))
            Text(
                style =
                    DaysTheme.typography.regular12
                        .copy(fontSize = 10.dp)
                        .toTextStyle(),
                text = DateTimeUtil.formatChatTime(message.createdAt),
                color = Color(0x80534C44),
            )
        }
    }
}

@Composable
fun CardMessageBubble(
    message: Message,
    profileImage: String,
    isMyMessage: Boolean,
    onCardClick: (Message) -> Unit,
) {
    ChatCard(
        content = message.content,
        isMyMessage = isMyMessage,
        profileImage = profileImage,
        onCardClick = { onCardClick(message) },
    )
}

@Composable
fun SystemMessageBubble(message: Message) {
    // 시스템 메시지 UI
}

fun getMessagePosition(
    messages: List<Message>,
    index: Int,
): MessagePosition {
    val prev = messages.getOrNull(index - 1)
    val next = messages.getOrNull(index + 1)
    val current = messages[index]

    return when {
        (prev?.senderUserId != current.senderUserId) && (next?.senderUserId == current.senderUserId) -> MessagePosition.LAST
        (prev?.senderUserId == current.senderUserId) && (next?.senderUserId == current.senderUserId) -> MessagePosition.MIDDLE
        (prev?.senderUserId == current.senderUserId) && (next?.senderUserId != current.senderUserId) -> MessagePosition.FIRST
        else -> MessagePosition.SINGLE
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
@Composable
private fun ChatBubblePreview() {
    Column {
        ChatBubble(
            message = generateDummyMessages(1)[0],
            isMyMessage = true,
            position = MessagePosition.SINGLE,
            profileImage = "",
            isNewMin = true,
        ) {}

        ChatBubble(
            message = generateDummyMessages(1)[0],
            isMyMessage = false,
            position = MessagePosition.SINGLE,
            profileImage = "",
            isNewMin = false,
        ) {}
    }
}
