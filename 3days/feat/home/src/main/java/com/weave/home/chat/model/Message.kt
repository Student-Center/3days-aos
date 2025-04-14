import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weave.model.domain.chat.Message
import com.weave.model.domain.chat.MessageContent
import com.weave.model.domain.chat.SystemMessageType
import java.util.UUID

@JsonClass(generateAdapter = true)
data class StompMessage(
    val id: String,
    val channelId: String,
    val senderUserId: String,
    val content: StompContent,
    val createdAt: String,
)

fun StompMessage.toMessage(): Message {
    return Message(
        id = UUID.fromString(id),
        content = content.toMessageContent(),
        senderUserId = UUID.fromString(senderUserId),
        channelId = UUID.fromString(channelId),
        createdAt = createdAt,
    )
}

fun StompContent.toMessageContent(): MessageContent {
    return MessageContent(
        type = enumValueOfOrNull<MessageContent.Type>(type.name),
        text = text,
        title = title,
        cardColor = cardColor?.name?.let { enumValueOfOrNull<MessageContent.CardColor>(it) },
        systemMessageType = systemType?.name?.let { enumValueOfOrNull<SystemMessageType>(it) },
        nextCardTitle = nextCardTitle,
    )
}

inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? =
    enumValues<T>().find { it.name == name }

@JsonClass(generateAdapter = true)
data class StompContent(
    val type: StompMessageType,
    val text: String?,
    val title: String?,
    val cardColor: StompCardColor?,
    val systemType: StompSystemMessageType?,
    val nextCardTitle: String?,
)

@JsonClass(generateAdapter = true)
data class StompSendMessage(
    val senderUserId: String,
    val messageContent: String,
    val messageType: String,
)

enum class StompSystemMessageType {
    @Json(name = "INFO") INFO,
    @Json(name = "NEXT_CARD") NEXT_CARD,
}

enum class StompMessageType {
    @Json(name = "TEXT") TEXT,
    @Json(name = "CARD") CARD,
    @Json(name = "SYSTEM") SYSTEM,
}

enum class StompCardColor {
    @Json(name = "BLUE") BLUE,
    @Json(name = "PINK") PINK,
}