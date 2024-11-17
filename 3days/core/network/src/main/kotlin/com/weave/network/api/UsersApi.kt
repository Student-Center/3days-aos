package com.weave.network.api

import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.ProfileWidget
import com.weave.network.model.ProfileWidgetType
import com.weave.network.model.RegisterUserRequest
import com.weave.network.model.TokenResponse
import com.weave.network.model.UpdateMyUserInfoRequest
import com.weave.network.model.UpdateMyUserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApi {
    /**
     * 프로필 위젯 삭제
     * 현재 사용자의 프로필 위젯을 삭제합니다.
     * Responses:
     *  - 204: 프로필 위젯 삭제 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 500: 서버 오류
     *
     * @param type 삭제할 프로필 위젯 타입
     * @return [Unit]
     */
    @DELETE("users/profileWidgets/{type}")
    suspend fun deleteProfileWidget(@Path("type") type: ProfileWidgetType): Response<Unit>

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
     * 프로필 위젯 추가 및 수정
     * 현재 사용자의 프로필 위젯을 추가 및 수정합니다.
     * Responses:
     *  - 200: 프로필 위젯 추가/수정 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 500: 서버 오류
     *
     * @param body
     * @return [ProfileWidget]
     */
    @PUT("users/profileWidgets")
    suspend fun putProfileWidget(@Body body: ProfileWidget): Response<ProfileWidget>

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
    suspend fun registerUser(
        @Header("X-Register-Token") xRegisterToken: kotlin.String,
        @Body registerUserRequest: RegisterUserRequest
    ): Response<TokenResponse>

    /**
     * 내 프로필 수정
     * 현재 로그인한 사용자의 프로필 정보를 수정합니다. (이름, 직군, 직장, 활동 지역)
     * Responses:
     *  - 200: 수정 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 500: 서버 오류
     *
     * @param updateMyUserInfoRequest
     * @return [UpdateMyUserInfoResponse]
     */
    @PATCH("users/my")
    suspend fun updateMyUserInfo(@Body updateMyUserInfoRequest: UpdateMyUserInfoRequest): Response<UpdateMyUserInfoResponse>

}
