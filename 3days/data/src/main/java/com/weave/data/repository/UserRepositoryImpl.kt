package com.weave.data.repository

import com.weave.data.datasource.UserRemoteDataSource
import com.weave.data.extension.handleNetworkCall
import com.weave.data.mapper.toDTO
import com.weave.data.mapper.toDomain
import com.weave.domain.repository.UserRepository
import com.weave.model.auth.AuthToken
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getMyUserInfo(): Flow<NetworkResult<MyInfo>> = handleNetworkCall(
        networkCall = {
            dataSource.getMyUserInfo()
        },
        mapToDomain = { it.toDomain }
    )

    override suspend fun putProfileWidget(type: ProfileWidget): Flow<NetworkResult<ProfileWidget>> =
        handleNetworkCall(
            networkCall = {
                dataSource.putProfileWidget(type.toDTO)
            },
            mapToDomain = { it.toDomain }
        )

    override suspend fun registerUser(
        xRegisterToken: String,
        registerInfo: RegisterInfo,
    ): Flow<NetworkResult<AuthToken>> = handleNetworkCall(
        networkCall = {
            dataSource.registerUser(
                xRegisterToken = xRegisterToken,
                registerUserRequest = registerInfo.toDTO
            )
        },
        mapToDomain = {
            it.toDomain
        }
    )
}