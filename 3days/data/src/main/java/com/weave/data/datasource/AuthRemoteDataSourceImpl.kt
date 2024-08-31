package com.weave.data.datasource

import com.weave.model.network.NetworkError
import com.weave.model.network.NetworkResult
import com.weave.network.api.AuthenticationApi
import com.weave.network.model.AuthVerifyPost200Response
import com.weave.network.model.PhoneRequest
import com.weave.network.model.RefreshRequest
import com.weave.network.model.RegisterRequest
import com.weave.network.model.TokenResponse
import com.weave.network.model.VerificationRequest
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val service: AuthenticationApi
): AuthRemoteDataSource {

    override suspend fun authPhonePost(phoneRequest: PhoneRequest): NetworkResult<Unit> {
        return try {
            val response = service.authPhonePost(phoneRequest)
            if(response.isSuccessful){
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(NetworkError.UNKNOWN)
            }
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun authRefreshPost(refreshRequest: RefreshRequest): NetworkResult<TokenResponse> {
        return try {
            val response = service.authRefreshPost(refreshRequest)
            if(response.isSuccessful){
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(NetworkError.UNKNOWN)
            }
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun authRegisterPost(registerRequest: RegisterRequest): NetworkResult<TokenResponse> {
        return try {
            val response = service.authRegisterPost(registerRequest)
            if(response.isSuccessful){
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(NetworkError.UNKNOWN)
            }
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun authVerifyPost(verificationRequest: VerificationRequest): NetworkResult<AuthVerifyPost200Response> {
        return try {
            val response = service.authVerifyPost(verificationRequest)
            if(response.isSuccessful){
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(NetworkError.UNKNOWN)
            }
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }
}