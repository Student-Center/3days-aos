package com.weave.model.domain.chat

data class Message(
    val id: java.util.UUID,
    val channelId: java.util.UUID,
    val senderUserId: java.util.UUID,
    val content: MessageContent,
    val createdAt: String
)