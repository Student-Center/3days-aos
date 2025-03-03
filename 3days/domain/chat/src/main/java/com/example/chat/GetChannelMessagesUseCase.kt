package com.example.chat

import com.weave.domain.repository.ChatRepository
import com.weave.model.domain.chat.Message
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class GetChannelMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        channelId: UUID,
        next: UUID? = null,
        limit: Int? = null
    ): Flow<NetworkResult<Pair<List<Message>, UUID?>>> =
        repository.getChannelMessages(channelId = channelId, next = next, limit = limit)
}