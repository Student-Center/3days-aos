package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.ProfileWidget
import com.weave.network.model.RegisterUserRequest
import com.weave.network.model.TokenResponse

interface UserRemoteDataSource {

    suspend fun getMyUserInfo(): NetworkResult<GetMyUserInfoResponse>

    suspend fun putProfileWidget(body: ProfileWidget): NetworkResult<ProfileWidget>

    suspend fun registerUser(
        xRegisterToken: String,
        registerUserRequest: RegisterUserRequest
    ): NetworkResult<TokenResponse>
}