package com.weave.network.api

import com.weave.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.google.gson.annotations.SerializedName

import com.weave.network.model.ErrorResponse
import com.weave.network.model.SendSystemMessageRequest

interface AdminApi {
    /**
     * 시스템 메시지 전송
     * 특정 채널에 시스템 메시지를 전송합니다.
     * Responses:
     *  - 201: 메시지 전송 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 404: 리소스를 찾을 수 없음
     *  - 500: 서버 오류
     *
     * @param channelId 채널 ID
     * @param sendSystemMessageRequest 
     * @return [Unit]
     */
    @POST("admin/chat/channels/{channelId}/system-message")
    suspend fun sendSystemMessage(@Path("channelId") channelId: java.util.UUID, @Body sendSystemMessageRequest: SendSystemMessageRequest): Response<Unit>

}
