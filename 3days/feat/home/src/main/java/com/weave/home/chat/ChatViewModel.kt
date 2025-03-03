package com.weave.home.chat

import StompMessage
import StompSendMessage
import androidx.lifecycle.viewModelScope
import com.example.chat.GetChannelMessagesUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.weave.auth.GetTokensUseCase
import com.weave.auth.RefreshTokenUseCase
import com.weave.design_system.component.SnackBarType
import com.weave.home.BuildConfig
import com.weave.model.domain.chat.Message
import com.weave.model.domain.user.MyInfo
import com.weave.user.GetMyInfoUseCase
import com.weave.utils.LoggerUtil
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import org.hildan.krossbow.stomp.conversions.typeRefOf
import org.hildan.krossbow.stomp.frame.StompFrame
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import toMessage
import java.time.Duration
import java.util.UUID
import javax.inject.Inject

sealed class ChatAction : UIAction {
    data class FetchData(val channelId: String) : ChatAction()
    data object LoadMessages : ChatAction()
    data class SendMessage(val message: String) : ChatAction()
}

sealed class ChatIntent : UIIntent {
    data class FetchData(val channelId: String) : ChatIntent()
    data object LoadMessages : ChatIntent()
    data class SendMessage(val message: String) : ChatIntent()
}

data class ChatState(
    val channelId: UUID? = null,
    val userInfo: MyInfo? = null,
    val messages: List<Message> = listOf(),
    val next: UUID? = null,
    var isLoading: Boolean = false,
    var hasMoreMessage: Boolean = true
) : UIState

sealed class ChatEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : ChatEffect()
}
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getChannelMessagesUseCase: GetChannelMessagesUseCase,
    private val getTokenUseCase: GetTokensUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : BaseViewModel<ChatAction, ChatIntent, ChatState, ChatEffect>(initialState = ChatState()) {

    private lateinit var accessToken: String
    private lateinit var stompSession: StompSession
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private val _newChatMessage = MutableSharedFlow<StompMessage>(replay = 1)
    val newChatMessage: SharedFlow<StompMessage> = _newChatMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            initializeSession()
            observeNewMessages()
        }
    }

    private suspend fun initializeSession() {
        accessToken = "Bearer ${getTokenUseCase.invoke().accessToken}"
        connectStomp()
    }

    private fun observeNewMessages() {
        viewModelScope.launch {
            newChatMessage.collect { newMessage ->
                val temp = listOf(newMessage.toMessage()) + uiState.messages
                setState { copy(messages = temp.distinctBy { it.id }) }
            }
        }
    }

    override fun actionPredicate(action: ChatAction): ChatIntent = when (action) {
        is ChatAction.FetchData -> ChatIntent.FetchData(action.channelId)
        is ChatAction.LoadMessages -> ChatIntent.LoadMessages
        is ChatAction.SendMessage -> ChatIntent.SendMessage(action.message)
    }

    override fun collectIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.FetchData -> fetchUserInfo(intent.channelId)
            is ChatIntent.LoadMessages -> fetchMessages()
            is ChatIntent.SendMessage -> sendMessage(intent.message)
        }
    }

    private fun fetchUserInfo(channelId: String) {
        setState { copy(channelId = UUID.fromString(channelId)) }
        viewModelScope.launch {
            getMyInfoUseCase.invoke().mapMerge().collect { data ->
                if (data != null) {
                    setState { copy(userInfo = data) }
                    fetchMessages()
                } else if(!isLoading){
                    showToast("내 정보 조회 실패")
                }
            }
        }
    }

    private fun fetchMessages() {
        if (uiState.isLoading || !uiState.hasMoreMessage) return
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            getChannelMessagesUseCase.invoke(uiState.channelId!!, uiState.next).mapMerge().collect { data ->
                if (data != null) {
                    updateMessages(*data.first.toTypedArray())
                    setState { copy(next = data.second, hasMoreMessage = data.second != null) }
                } else if(!isLoading) {
                    showToast("채팅 내역 조회 실패")
                }
                setState { copy(isLoading = false) }
            }
        }
    }

    private fun updateMessages(vararg newMessages: Message) {
        setState { copy(messages = (messages + newMessages).distinctBy { it.id }) }
    }

    private suspend fun connectStomp() {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(10))
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        val client = StompClient(OkHttpWebSocketClient(okHttpClient))

        flow {
            emit(client.connect(BuildConfig.SOCKET_URL, customStompConnectHeaders = mapOf(HEADER_AUTHORIZATION to accessToken)).withMoshi(moshi))
        }.retryWhen { cause, attempt ->
            handleConnectionRetry(cause, attempt)
        }.collect { session ->
            stompSession = session
            subscribeToMessages()
        }
    }

    private fun handleConnectionRetry(cause: Throwable, attempt: Long): Boolean {
        if (attempt < 3) {
            LoggerUtil.error("웹소켓 연결 실패, 재시도 ($attempt): ${cause.message}")
            viewModelScope.launch { delay(2000) }
            if (attempt == 2L) refreshToken()
            return true
        }
        LoggerUtil.error("웹소켓 연결 실패: ${cause.message}")
        return false
    }

    private fun refreshToken() {
        viewModelScope.launch {
            refreshTokenUseCase.invoke().mapMerge().collect {
                it?.let { accessToken = "Bearer ${it.accessToken}" }
            }
        }
    }

    private fun subscribeToMessages() {
        viewModelScope.launch {
            stompSession.subscribe(StompSubscribeHeaders(SUBSCRIBE_URL.format(uiState.channelId), customHeaders = mapOf(HEADER_AUTHORIZATION to accessToken)))
                .catch { connectStomp() }
                .collect { frame -> handleIncomingMessage(frame) }
        }
    }

    private suspend fun handleIncomingMessage(frame: StompFrame) {
        runCatching {
            moshi.adapter(StompMessage::class.java).fromJson(frame.bodyAsText)?.let { _newChatMessage.emit(it) }
        }.onFailure { LoggerUtil.error("JSON 파싱 오류: ${it.message}") }
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            if (!::stompSession.isInitialized) {
                LoggerUtil.error("세션이 초기화되지 않음. 재연결 시도")
                connectStomp()
                return@launch
            }
            runCatching {
                stompSession.withMoshi(moshi).convertAndSend(
                    StompSendHeaders(SEND_URL.format(uiState.channelId), customHeaders = mapOf(HEADER_AUTHORIZATION to accessToken)),
                    StompSendMessage(senderUserId = uiState.userInfo?.id.toString(), messageContent = message, messageType = "TEXT"),
                    bodyType = typeRefOf()
                )
            }.onFailure {
                LoggerUtil.error("메시지 전송 실패: ${it.message}, 재연결 시도")
                connectStomp()
            }
        }
    }

    private fun showToast(message: String) {
        setEffect { ChatEffect.ShowToast(message, SnackBarType.ERROR) }
    }

    override fun onCleared() {
        viewModelScope.launch { stompSession.disconnect() }
        super.onCleared()
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val SUBSCRIBE_URL = "/channel/%s"
        private const val SEND_URL = "/app/channel/%s"
    }
}
