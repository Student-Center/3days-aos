package com.weave.data.datasource

import com.weave.model.network.NetworkError
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
    private val service: AuthApi
): AuthRemoteDataSource {

    override suspend fun existingUserVerifyCode(
        authCodeId: UUID,
        verifyCodeRequest: VerifyCodeRequest
    ): NetworkResult<ExistingUserVerifyCodeResponse> {
        return try {
            val response = service.existingUserVerifyCode(authCodeId, verifyCodeRequest)

            if(response.isSuccessful){
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(NetworkError.getNetworkErrorByCode(response.code()))
            }
        } catch (e: Exception){
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun newUserVerifyCode(
        authCodeId: UUID,
        verifyCodeRequest: VerifyCodeRequest
    ): NetworkResult<NewUserVerifyCodeResponse> {
        return try {
            val response = service.newUserVerifyCode(authCodeId, verifyCodeRequest)

            if(response.isSuccessful){
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(NetworkError.getNetworkErrorByCode(response.code()))
            }
        } catch (e: Exception){
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): NetworkResult<TokenResponse> {
        return try {
            val response = service.refreshToken(refreshTokenRequest)

            if(response.isSuccessful){
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(NetworkError.getNetworkErrorByCode(response.code()))
            }
        } catch (e: Exception){
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun requestVerification(sendAuthCodeRequest: SendAuthCodeRequest): NetworkResult<SendAuthCodeResponse> {
        return try {
            val response = service.requestVerification(OSType.AOS, sendAuthCodeRequest)

            if(response.isSuccessful){
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(NetworkError.getNetworkErrorByCode(response.code()))
            }
        } catch (e: Exception){
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }
}