import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weave.model.domain.chat.Message
import com.weave.model.domain.chat.MessageContent
import com.weave.utils.LoggerUtil
import java.util.UUID

@JsonClass(generateAdapter = true)
data class StompMessage(
    val id: String,
    val channelId: String,
    val senderUserId: String,
    val content: Content,
    val createdAt: String
)

fun StompMessage.toMessage(): Message {
    LoggerUtil.info("toMessage")
    return Message(
        id = UUID.fromString(this.id),
        content = this.content.let {
            MessageContent(
                type = MessageContent.Type.entries.find { entry -> it.type.name == entry.name }, text = it.text, cardColor = null
        )},
        senderUserId = UUID.fromString(this.senderUserId),
        channelId = UUID.fromString(this.channelId),
        createdAt = this.createdAt
    )
}


@JsonClass(generateAdapter = true)
data class Content(
    val type: MessageType,
    val text: String,
    val cardColor: CardColor?
)

@JsonClass(generateAdapter = true)
data class StompSendMessage(
    val senderUserId: String,
    val messageContent: String,
    val messageType: String
)

enum class MessageType {
    @Json(name = "Text") TEXT,
    @Json(name = "Card") CARD
}

enum class CardColor {
    @Json(name = "Blue") BLUE,
    @Json(name = "Pink") PINK
}


