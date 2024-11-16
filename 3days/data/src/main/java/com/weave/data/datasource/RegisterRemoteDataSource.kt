package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.RegisterUserRequest
import com.weave.network.model.TokenResponse

interface RegisterRemoteDataSource {

    suspend fun registerUser(
        xRegisterToken: String,
        registerUserRequest: RegisterUserRequest
    ): NetworkResult<TokenResponse>
}