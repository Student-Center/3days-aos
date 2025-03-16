package com.weave.home.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.component.DaysBackgroundTextureImage
import com.weave.design_system.component.DaysSnackBarHost
import com.weave.home.chat.resource.ChatBubble
import com.weave.home.chat.resource.ChatHeader
import com.weave.home.chat.resource.ChatInput
import com.weave.home.chat.resource.DayDivider
import com.weave.home.chat.resource.MessagePosition
import com.weave.home.chat.resource.getMessagePosition
import com.weave.home.profile.main.SnackBarViewModel
import com.weave.model.domain.chat.Message
import com.weave.model.domain.chat.MessageContent
import com.weave.utils.DateTimeUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    channelId: String,
    moveToHome: () -> Unit
) {
    val uiState = viewModel.uiState
    val listState = rememberLazyListState()
    val trigger by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val focusManager = LocalFocusManager.current
    val messageState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.setAction(ChatAction.FetchData(channelId)) }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ChatEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    if (uiState.messages.isNotEmpty()) {
        LaunchedEffect(trigger) {
            val totalItemCount = uiState.messages.size
            val visibleItemCount = listState.layoutInfo.visibleItemsInfo.size
            val lastVisibleItemIndex = listState.firstVisibleItemIndex + visibleItemCount

            if (lastVisibleItemIndex >= totalItemCount - 1) {
                viewModel.setAction(ChatAction.LoadMessages)
            }
        }
    }


    ChatScreenContent(
        listState = listState,
        messageState = messageState.value,
        snackBarHostState = snackBarViewModel.snackBarHostState,
        uiState = uiState,
        focusManager = focusManager,
        moveToHome = moveToHome,
        onTextChange = { messageState.value = it },
        onSend = {
            viewModel.setAction(ChatAction.SendMessage(messageState.value))
            messageState.value = ""
        }
    )
}

@Composable
private fun ChatScreenContent(
    listState: LazyListState,
    messageState: String,
    snackBarHostState: SnackbarHostState,
    uiState: ChatState,
    focusManager: FocusManager,
    moveToHome: () -> Unit,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val partnerProfileImage = uiState.userInfo?.profileImages?.firstOrNull()?.url?.toString() ?: ""

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            DaysSnackBarHost(
                snackState = snackBarHostState,
                modifier = Modifier.padding(bottom = 36.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DaysBackgroundTextureImage()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = padding.calculateBottomPadding())
                    .consumeWindowInsets(padding)
                    .imePadding()
            ) {
                ChatHeader(
                    topPadding = padding.calculateTopPadding(),
                    profileImage = partnerProfileImage,
                    moveToHome = moveToHome
                )

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Box(modifier = Modifier.weight(1f)) {
                        ChatBoard(
                            listState = listState,
                            messages = uiState.messages,
                            userId = uiState.userInfo?.id ?: UUID.randomUUID(),
                            profileImage = partnerProfileImage
                        )
                    }

                    ChatInput(
                        textState = messageState,
                        focusManager = focusManager,
                        onTextChange = onTextChange,
                        onSend = onSend
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBoard(
    listState: LazyListState,
    messages: List<Message>,
    userId: UUID,
    profileImage: String,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        reverseLayout = true,
        state = listState
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        itemsIndexed(messages) { index, message ->
            val position = getMessagePosition(messages, index)
            val nextMessage = messages.getOrNull(index + 1)

            val isNewDay = nextMessage?.let {
                val currentDate = message.createdAt.take(10).replace("-", "").toIntOrNull()
                val nextDate = it.createdAt.take(10).replace("-", "").toIntOrNull()
                currentDate != nextDate
            } ?: true

            if (isNewDay) {
                DayDivider(value = message.createdAt)
            }

            ChatBubble(
                message = message,
                position = position,
                isMyMessage = message.senderUserId == userId,
                profileImage = profileImage
            )

            if (position == MessagePosition.SINGLE || position == MessagePosition.FIRST) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }

            if (position == MessagePosition.MIDDLE || position == MessagePosition.LAST) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
private fun ChatScreenPreview() {
    ChatScreenContent(
        listState = LazyListState(),
        messageState = "",
        snackBarHostState = SnackbarHostState(),
        uiState = ChatState(),
        moveToHome = {},
        focusManager = LocalFocusManager.current,
        onTextChange = { },
        onSend = { }
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
private fun ChatBoardPreview() {
    ChatBoard(
        listState = LazyListState(),
        userId = UUID.fromString("11111111-1111-1111-1111-111111111111"),
        messages = generateDummyMessages(20),
        profileImage = "",
    )
}

fun generateDummyMessages(count: Int): List<Message> {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val baseDate = LocalDateTime.of(2025, 3, 2, 10, 38, 24, 0) // 시작 날짜

    return List(count) { index ->
        val currentDate = baseDate.plusDays((index / 4).toLong()) // 4개마다 날짜 증가
        val formattedDate = currentDate.format(dateFormatter)

        val microsecond = (index % 1000000).toString().padStart(6, '0')

        val createdAt = "$formattedDate.$microsecond"

        Message(
            id = UUID.randomUUID(),
            channelId = UUID.randomUUID(),
            senderUserId = if ((1..2).random() == 1) UUID.fromString("11111111-1111-1111-1111-111111111111") else UUID.fromString(
                "21111111-1111-1111-1111-111111111111"
            ),
            content =
            if (index % 2 == 0) {
                MessageContent(type = MessageContent.Type.TEXT, text = "메시지 ${index + 1}")
            } else {
                MessageContent(type = MessageContent.Type.TEXT, text = "메시지 ${index + 1}".repeat(8))
            },
            createdAt = createdAt
        )
    }
}


