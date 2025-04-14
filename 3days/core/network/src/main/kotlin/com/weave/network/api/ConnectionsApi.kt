package com.weave.network.api

import com.weave.network.model.GetMyConnectionResponse
import retrofit2.Response
import retrofit2.http.GET

interface ConnectionsApi {
    /**
     * 자신의 현재 커넥션을 조회합니다.
     * 자신의 현재 커넥션을 조회합니다.
     * Responses:
     *  - 200: 커넥션 조회 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 404: 리소스를 찾을 수 없음
     *
     * @return [GetMyConnectionResponse]
     */
    @GET("connections/my/current")
    suspend fun connectionsMyCurrentGet(): Response<GetMyConnectionResponse>
}
