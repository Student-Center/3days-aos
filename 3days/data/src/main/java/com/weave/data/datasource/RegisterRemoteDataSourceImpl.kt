package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.model.network.NetworkResult
import com.weave.network.api.UsersApi
import com.weave.network.model.RegisterUserRequest
import com.weave.network.model.TokenResponse
import javax.inject.Inject

class RegisterRemoteDataSourceImpl @Inject constructor(
    private val service: UsersApi
) : RegisterRemoteDataSource {

    override suspend fun registerUser(
        xRegisterToken: String,
        registerUserRequest: RegisterUserRequest
    ): NetworkResult<TokenResponse> {
        return handleApiResponse {
            service.registerUser(xRegisterToken, registerUserRequest)
        }
    }
}