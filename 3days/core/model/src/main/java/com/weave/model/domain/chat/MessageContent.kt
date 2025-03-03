package com.weave.model.domain.chat

data class MessageContent(
    val type: Type? = null,
    val text: String? = null,
    val cardColor: String? = null

) {

    enum class Type(val value: String) {
        TEXT("TEXT"),
        CARD("CARD");
    }
}