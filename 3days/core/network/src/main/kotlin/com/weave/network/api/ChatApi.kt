package com.weave.network.api

import com.weave.network.model.GetChannelMessagesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    /**
     * 채널 메시지 목록 조회
     * 특정 채널의 메시지 목록을 최신순으로 조회합니다. (무한스크롤)
     * Responses:
     *  - 200: 메시지 목록 조회 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 404: 리소스를 찾을 수 없음
     *
     * @param channelId 채널 ID
     * @param next 페이지네이션을 위한 다음 검색 시작 ID (optional)
     * @param limit 반환할 최대 결과 수 (optional, default to 20)
     * @return [GetChannelMessagesResponse]
     */
    @GET("chat/channels/{channelId}/messages")
    suspend fun getChannelMessages(
        @Path("channelId") channelId: java.util.UUID,
        @Query("next") next: java.util.UUID? = null,
        @Query("limit") limit: kotlin.Int? = 20,
    ): Response<GetChannelMessagesResponse>
}
