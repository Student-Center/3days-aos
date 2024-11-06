package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.model.network.NetworkResult
import com.weave.network.api.UsersApi
import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.ProfileWidget
import com.weave.network.model.RegisterUserRequest
import com.weave.network.model.TokenResponse
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val service: UsersApi
) : UserRemoteDataSource {

    override suspend fun getMyUserInfo(): NetworkResult<GetMyUserInfoResponse> {
        return handleApiResponse {
            service.getMyUserInfo()
        }
    }

    override suspend fun putProfileWidget(body: ProfileWidget): NetworkResult<ProfileWidget> {
        return handleApiResponse {
            service.putProfileWidget(body)
        }
    }

    override suspend fun registerUser(
        xRegisterToken: String,
        registerUserRequest: RegisterUserRequest
    ): NetworkResult<TokenResponse> {
        return handleApiResponse {
            service.registerUser(xRegisterToken, registerUserRequest)
        }
    }
}