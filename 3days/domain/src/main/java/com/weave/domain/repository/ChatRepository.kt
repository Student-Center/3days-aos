package com.weave.domain.repository

import com.weave.model.domain.chat.Message
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ChatRepository {

    suspend fun getChannelMessages(
        channelId: UUID,
        next: UUID?,
        limit: Int?
    ): Flow<NetworkResult<Pair<List<Message>, UUID?>>>
}