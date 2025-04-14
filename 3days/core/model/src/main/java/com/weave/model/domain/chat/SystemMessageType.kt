package com.weave.model.domain.chat

enum class SystemMessageType(
    val value: String,
) {
    INFO("INFO"),
    NEXT_CARD("NEXT_CARD"),
    ;

    override fun toString(): String = value
}
