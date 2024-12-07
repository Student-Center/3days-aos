package com.weave.network.api

import com.weave.network.model.CompleteProfileImageUploadRequest
import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.GetProfileImageUploadUrlResponse
import com.weave.network.model.ProfileImageExtension
import com.weave.network.model.ProfileWidget
import com.weave.network.model.ProfileWidgetType
import com.weave.network.model.RegisterUserRequest
import com.weave.network.model.TokenResponse
import com.weave.network.model.UpdateMyUserInfoRequest
import com.weave.network.model.UpdateMyUserInfoResponse
import com.weave.network.model.UpdateUserDesiredPartnerRequest
import com.weave.network.model.UpdateUserDesiredPartnerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    /**
     * 프로필 이미지 업로드 완료 콜백
     * 프로필 이미지 업로드 완료 후 콜백을 받아 이미지를 등록합니다. 업로드 완료후 일정시간 이내에 콜백이 호출되지 않을경우 이미지는 삭제됩니다.
     * Responses:
     *  - 200: 이미지 등록 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 500: 서버 오류
     *
     * @param completeProfileImageUploadRequest
     * @return [Unit]
     */
    @POST("users/my/profile-images/upload-complete")
    suspend fun completeProfileImageUpload(@Body completeProfileImageUploadRequest: CompleteProfileImageUploadRequest): Response<Unit>

    /**
     * 프로필 이미지 삭제
     * 특정 프로필 이미지를 삭제합니다.
     * Responses:
     *  - 204: 삭제 성공
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 404: 리소스를 찾을 수 없음
     *  - 500: 서버 오류
     *
     * @param imageId 삭제할 프로필 이미지 ID
     * @return [Unit]
     */
    @DELETE("users/my/profile-images/{imageId}")
    suspend fun deleteProfileImage(@Path("imageId") imageId: java.util.UUID): Response<Unit>

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
     * 프로필 이미지 업로드 URL 생성
     * 프로필 이미지 업로드를 위한 URL을 생성합니다.
     * Responses:
     *  - 200: URL 생성 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 500: 서버 오류
     *
     * @param extension 이미지 확장자
     * @return [GetProfileImageUploadUrlResponse]
     */
    @GET("users/my/profile-images/upload-url")
    suspend fun getProfileImageUploadUrl(@Query("extension") extension: ProfileImageExtension): Response<GetProfileImageUploadUrlResponse>

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
     * 내 원하는 파트너 수정
     * 현재 로그인한 사용자의 원하는 파트너 정보를 수정합니다.
     * Responses:
     *  - 200: 수정 성공
     *  - 400: 잘못된 요청
     *  - 401: 인증 실패 (토큰 만료 또는 유효하지 않은 토큰)
     *  - 500: 서버 오류
     *
     * @param updateUserDesiredPartnerRequest
     * @return [UpdateUserDesiredPartnerResponse]
     */
    @PUT("users/my/desiredPartner")
    suspend fun updateMyDesiredPartner(@Body updateUserDesiredPartnerRequest: UpdateUserDesiredPartnerRequest): Response<UpdateUserDesiredPartnerResponse>

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
    @PUT("users/my")
    suspend fun updateMyUserInfo(@Body updateMyUserInfoRequest: UpdateMyUserInfoRequest): Response<UpdateMyUserInfoResponse>

}
