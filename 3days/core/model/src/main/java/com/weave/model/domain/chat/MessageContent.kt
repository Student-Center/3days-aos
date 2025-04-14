package com.weave.model.domain.chat

data class MessageContent(
    val type: Type? = null,
    val text: String? = null,
    val title: String? = null,
    val cardColor: CardColor? = null,
    val systemMessageType: SystemMessageType? = null,
    val nextCardTitle: String? = null,
) {
    enum class Type(
        val value: String,
    ) {
        TEXT("TEXT"),
        CARD("CARD"),
        SYSTEM("SYSTEM"),
    }

    enum class CardColor(
        val value: String,
    ) {
        BLUE("BLUE"),
        PINK("PINK"),
    }
}
