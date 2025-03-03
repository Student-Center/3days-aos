package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.GetChannelMessagesResponse

interface ChatRemoteDataSource {

    suspend fun getChannelMessages(
        channelId: java.util.UUID,
        next: java.util.UUID? = null,
        limit: Int? = 20
    ): NetworkResult<GetChannelMessagesResponse>
}