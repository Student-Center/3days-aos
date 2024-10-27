package com.weave.network.api

import com.weave.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.google.gson.annotations.SerializedName

import com.weave.network.model.ErrorResponse
import com.weave.network.model.ExistingUserVerifyCodeResponse
import com.weave.network.model.NewUserVerifyCodeResponse
import com.weave.network.model.OSType
import com.weave.network.model.RefreshTokenRequest
import com.weave.network.model.SendAuthCodeRequest
import com.weave.network.model.SendAuthCodeResponse
import com.weave.network.model.TokenResponse
import com.weave.network.model.VerifyCodeRequest

interface AuthApi {
    /**
     * SMS 인증 코드 확인 (기존 유저)
     * 발급된 인증 코드 아이디(authCodeId)와 SMS 인증 코드로 인증을 완료합니다.
     * Responses:
     *  - 200: 인증 완료 및 토큰 발급
     *  - 400: 잘못된 요청
     *  - 404: 리소스를 찾을 수 없음
     *  - 500: 서버 오류
     *
     * @param authCodeId 인증 요청 시 발급된 고유 식별자 (UUID 형식)
     * @param verifyCodeRequest 
     * @return [ExistingUserVerifyCodeResponse]
     */
    @POST("auth/codes/{authCodeId}/existingUser")
    suspend fun existingUserVerifyCode(@Path("authCodeId") authCodeId: java.util.UUID, @Body verifyCodeRequest: VerifyCodeRequest): Response<ExistingUserVerifyCodeResponse>

    /**
     * SMS 인증 코드 확인 (신규 유저)
     * 발급된 인증 코드 아이디(authCodeId)와 SMS 인증 코드로 인증을 완료합니다.
     * Responses:
     *  - 200: 인증 완료 및 토큰 발급
     *  - 400: 잘못된 요청
     *  - 404: 리소스를 찾을 수 없음
     *  - 500: 서버 오류
     *
     * @param authCodeId 인증 요청 시 발급된 고유 식별자 (UUID 형식)
     * @param verifyCodeRequest 
     * @return [NewUserVerifyCodeResponse]
     */
    @POST("auth/codes/{authCodeId}/newUser")
    suspend fun newUserVerifyCode(@Path("authCodeId") authCodeId: java.util.UUID, @Body verifyCodeRequest: VerifyCodeRequest): Response<NewUserVerifyCodeResponse>

    /**
     * 액세스 토큰 갱신
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.
     * Responses:
     *  - 200: 토큰 갱신 성공
     *  - 400: 유효하지 않은 리프레시 토큰
     *  - 401: 리프레시 토큰 만료
     *  - 500: 서버 오류
     *
     * @param refreshTokenRequest 
     * @return [TokenResponse]
     */
    @POST("auth/token/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<TokenResponse>

    /**
     * SMS 인증 요청
     * 회원 가입 또는 로그인을 위한 SMS 인증을 요청합니다.
     * Responses:
     *  - 201: 인증 요청 성공
     *  - 400: 잘못된 요청
     *
     * @param xOSType 사용자의 OS 유형 (IOS 또는 AOS)
     * @param sendAuthCodeRequest 
     * @return [SendAuthCodeResponse]
     */
    @POST("auth/codes")
    suspend fun requestVerification(@Header("X-OS-Type") xOSType: OSType, @Body sendAuthCodeRequest: SendAuthCodeRequest): Response<SendAuthCodeResponse>

}
