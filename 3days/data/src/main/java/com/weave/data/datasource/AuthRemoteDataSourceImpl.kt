package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.model.network.NetworkResult
import com.weave.network.api.AuthApi
import com.weave.network.model.ExistingUserVerifyCodeResponse
import com.weave.network.model.NewUserVerifyCodeResponse
import com.weave.network.model.OSType
import com.weave.network.model.RefreshTokenRequest
import com.weave.network.model.SendAuthCodeRequest
import com.weave.network.model.SendAuthCodeResponse
import com.weave.network.model.TokenResponse
import com.weave.network.model.VerifyCodeRequest
import java.util.UUID
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthApi,
) : AuthRemoteDataSource {

    override suspend fun existingUserVerifyCode(
        authCodeId: UUID,
        verifyCodeRequest: VerifyCodeRequest
    ): NetworkResult<ExistingUserVerifyCodeResponse> {
        return handleApiResponse {
            authService.existingUserVerifyCode(authCodeId, verifyCodeRequest)
        }
    }

    override suspend fun newUserVerifyCode(
        authCodeId: UUID,
        verifyCodeRequest: VerifyCodeRequest
    ): NetworkResult<NewUserVerifyCodeResponse> {
        return handleApiResponse {
            authService.newUserVerifyCode(authCodeId, verifyCodeRequest)
        }
    }

    override suspend fun refreshToken(body: RefreshTokenRequest): NetworkResult<TokenResponse> {
        return handleApiResponse {
            authService.refreshToken(body)
        }
    }

    override suspend fun requestVerification(sendAuthCodeRequest: SendAuthCodeRequest): NetworkResult<SendAuthCodeResponse> {
        return handleApiResponse {
            authService.requestVerification(OSType.AOS, sendAuthCodeRequest)
        }
    }
}