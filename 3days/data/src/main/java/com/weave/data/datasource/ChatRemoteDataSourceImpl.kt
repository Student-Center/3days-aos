package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.model.network.NetworkResult
import com.weave.network.api.ChatApi
import com.weave.network.model.GetChannelMessagesResponse
import java.util.UUID
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val service: ChatApi
) : ChatRemoteDataSource {

    override suspend fun getChannelMessages(
        channelId: UUID,
        next: UUID?,
        limit: Int?
    ): NetworkResult<GetChannelMessagesResponse> {
        return handleApiResponse {
            service.getChannelMessages(channelId, next, limit)
        }
    }
}