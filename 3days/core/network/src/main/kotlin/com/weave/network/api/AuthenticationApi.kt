package com.weave.network.api

import retrofit2.http.*
import retrofit2.Response

import com.weave.network.model.AuthVerifyPost200Response
import com.weave.network.model.PhoneRequest
import com.weave.network.model.RefreshRequest
import com.weave.network.model.RegisterRequest
import com.weave.network.model.TokenResponse
import com.weave.network.model.VerificationRequest

interface AuthenticationApi {
    /**
     * 휴대폰 번호 인증 요청
     * 사용자의 휴대폰 번호로 인증 코드를 발송합니다.
     * Responses:
     *  - 200: 인증 코드가 성공적으로 발송됨
     *  - 400: 유효하지 않은 휴대폰 번호
     *
     * @param phoneRequest 
     * @return [Unit]
     */
    @POST("auth/phone")
    suspend fun authPhonePost(@Body phoneRequest: PhoneRequest): Response<Unit>

    /**
     * 액세스 토큰 갱신
     * 리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급받습니다.
     * Responses:
     *  - 200: 새로운 액세스 토큰 발급 성공
     *  - 401: 유효하지 않은 리프레시 토큰
     *
     * @param refreshRequest 
     * @return [TokenResponse]
     */
    @POST("auth/refresh")
    suspend fun authRefreshPost(@Body refreshRequest: RefreshRequest): Response<TokenResponse>

    /**
     * 회원가입 완료
     * Register Token과 사용자 프로필 정보를 이용하여 회원가입을 완료합니다.
     * Responses:
     *  - 200: 회원가입 성공
     *  - 400: 유효하지 않은 Register Token 또는 프로필 정보
     *
     * @param registerRequest 
     * @return [TokenResponse]
     */
    @POST("auth/register")
    suspend fun authRegisterPost(@Body registerRequest: RegisterRequest): Response<TokenResponse>

    /**
     * 인증 코드 확인
     * 사용자가 입력한 인증 코드의 유효성을 확인합니다.
     * Responses:
     *  - 200: 휴대폰 번호 인증 성공
     *  - 400: 유효하지 않은 인증 코드
     *
     * @param verificationRequest 
     * @return [AuthVerifyPost200Response]
     */
    @POST("auth/verify")
    suspend fun authVerifyPost(@Body verificationRequest: VerificationRequest): Response<AuthVerifyPost200Response>

}
