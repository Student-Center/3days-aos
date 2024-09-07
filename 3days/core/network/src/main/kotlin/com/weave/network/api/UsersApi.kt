package com.weave.network.api

import com.weave.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.google.gson.annotations.SerializedName

import com.weave.network.model.RefreshTokenRequest
import com.weave.network.model.RequestVerification201Response
import com.weave.network.model.RequestVerificationRequest
import com.weave.network.model.TokenResponse
import com.weave.network.model.UserRegistration
import com.weave.network.model.VerifyCode200Response
import com.weave.network.model.VerifyCodeRequest

interface UsersApi {
    /**
     * 액세스 토큰 갱신
     * - 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다. 
     * Responses:
     *  - 200: 토큰 갱신 성공
     *  - 401: 유효하지 않거나 만료된 리프레시 토큰
     *
     * @param refreshTokenRequest 
     * @return [TokenResponse]
     */
    @POST("users/token/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<TokenResponse>

    /**
     * 회원 가입
     * - SMS 인증 시 발급된 registerToken을 이용하여 회원 가입을 완료합니다. - 회원 가입 완료 시 accessToken과 refreshToken을 발급합니다. 
     * Responses:
     *  - 201: 회원 가입 성공
     *  - 400: 잘못된 입력 데이터
     *  - 401: 유효하지 않거나 만료된 registerToken
     *  - 409: 이미 존재하는 사용자
     *
     * @param userRegistration 
     * @return [TokenResponse]
     */
    @POST("users")
    suspend fun registerUser(@Body userRegistration: UserRegistration): Response<TokenResponse>

    /**
     * SMS 인증 요청
     * - 회원 가입 또는 로그인 토큰 발급을 위한 SMS 인증을 요청합니다. 
     * Responses:
     *  - 201: 인증 요청 성공
     *  - 400: 잘못된 전화번호 형식
     *
     * @param requestVerificationRequest 
     * @return [RequestVerification201Response]
     */
    @POST("users/verifications")
    suspend fun requestVerification(@Body requestVerificationRequest: RequestVerificationRequest): Response<RequestVerification201Response>

    /**
     * SMS 인증 코드 확인
     * - SMS 인증 요청 시 발급된 verificationId와 함께 인증 코드를 입력하여 인증을 완료합니다. - 새 사용자의 경우 회원 가입을 위한 registerToken을 발급합니다. - 기존 사용자의 경우 로그인을 위한 accessToken과 refreshToken을 발급합니다. 
     * Responses:
     *  - 200: 인증 완료
     *  - 400: 잘못되거나 만료된 인증 코드
     *  - 404: 존재하지 않는 verificationId
     *
     * @param verificationId 인증 요청 시 발급된 고유 식별자 (UUID 형식)
     * @param verifyCodeRequest 
     * @return [VerifyCode200Response]
     */
    @PUT("users/verifications/{verificationId}")
    suspend fun verifyCode(@Path("verificationId") verificationId: java.util.UUID, @Body verifyCodeRequest: VerifyCodeRequest): Response<VerifyCode200Response>

}
