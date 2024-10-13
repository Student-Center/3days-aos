package com.weave.network.api

import com.weave.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.google.gson.annotations.SerializedName

import com.weave.network.model.ErrorResponse
import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.RegisterUserRequest
import com.weave.network.model.TokenResponse

interface UsersApi {
    /**
     * 내 프로필 조회
     * 현재 로그인한 사용자의 프로필 정보를 조회합니다.
     * Responses:
     *  - 200: 조회 성공
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 500: 서버 오류
     *
     * @return [GetMyUserInfoResponse]
     */
    @GET("users/my")
    suspend fun getMyUserInfo(): Response<GetMyUserInfoResponse>

    /**
     * 회원 가입
     * SMS 인증 후 발급된 registerToken으로 회원 가입을 완료합니다.
     * Responses:
     *  - 201: 회원 가입 성공
     *  - 400: 잘못된 요청
     *  - 401: 회원 가입 토큰 만료
     *  - 500: 서버 오류
     *
     * @param xRegisterToken 번호 인증 후 발급받은 회원가입용 토큰
     * @param registerUserRequest 
     * @return [TokenResponse]
     */
    @POST("users")
    suspend fun registerUser(@Header("X-Register-Token") xRegisterToken: kotlin.String, @Body registerUserRequest: RegisterUserRequest): Response<TokenResponse>

}
