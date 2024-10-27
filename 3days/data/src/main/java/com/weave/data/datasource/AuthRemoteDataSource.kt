package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.ExistingUserVerifyCodeResponse
import com.weave.network.model.NewUserVerifyCodeResponse
import com.weave.network.model.RefreshTokenRequest
import com.weave.network.model.SendAuthCodeRequest
import com.weave.network.model.SendAuthCodeResponse
import com.weave.network.model.TokenResponse
import com.weave.network.model.VerifyCodeRequest
import java.util.UUID

interface AuthRemoteDataSource {

    suspend fun existingUserVerifyCode(authCodeId: UUID, verifyCodeRequest: VerifyCodeRequest): NetworkResult<ExistingUserVerifyCodeResponse>

    suspend fun newUserVerifyCode(authCodeId: UUID, verifyCodeRequest: VerifyCodeRequest): NetworkResult<NewUserVerifyCodeResponse>

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): NetworkResult<TokenResponse>

    suspend fun requestVerification(sendAuthCodeRequest: SendAuthCodeRequest): NetworkResult<SendAuthCodeResponse>
}