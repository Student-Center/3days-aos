package com.weave.data.repository

import com.weave.data.datasource.ChatRemoteDataSource
import com.weave.data.extension.handleNetworkCall
import com.weave.domain.repository.ChatRepository
import com.weave.model.domain.chat.Message
import com.weave.model.domain.chat.MessageContent
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val dataSource: ChatRemoteDataSource
) : ChatRepository {

    override suspend fun getChannelMessages(
        channelId: UUID,
        next: UUID?,
        limit: Int?
    ): Flow<NetworkResult<Pair<List<Message>, UUID?>>> = handleNetworkCall(
        networkCall = { dataSource.getChannelMessages(channelId, next, limit) },
        mapToDomain = {
            Pair(it.messages!!.map { message ->
                Message(
                    id = message.id,
                    channelId = message.channelId,
                    senderUserId = message.senderUserId,
                    content = message.content.let { content ->
                        MessageContent(
                            text = content.text,
                            cardColor = content.cardColor,
                            type = content.type.let { type -> MessageContent.Type.entries.find { entry -> entry.value == type?.value } } ?: MessageContent.Type.TEXT
                        )
                    },
                    createdAt = message.createdAt
                )
            }, it.next)
        }
    )
}