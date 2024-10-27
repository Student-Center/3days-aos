package com.weave.data.repository

import com.weave.data.datasource.AuthRemoteDataSource
import com.weave.data.extension.handleNetworkCall
import com.weave.data.mapper.toDomain
import com.weave.domain.repository.AuthRepository
import com.weave.model.auth.AuthRegisterToken
import com.weave.model.auth.AuthToken
import com.weave.model.auth.AuthVerifyToken
import com.weave.model.network.NetworkResult
import com.weave.network.model.RefreshTokenRequest
import com.weave.network.model.SendAuthCodeRequest
import com.weave.network.model.VerifyCodeRequest
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun existingUserVerifyCode(
        authCodeId: UUID,
        verifyCode: String
    ): Flow<NetworkResult<AuthToken>> = handleNetworkCall(
        networkCall = {
            dataSource.existingUserVerifyCode(
                authCodeId = authCodeId,
                verifyCodeRequest = VerifyCodeRequest(verifyCode)
            )
        },
        mapToDomain = { it.toDomain }
    )

    override suspend fun newUserVerifyCode(
        authCodeId: UUID,
        verifyCode: String
    ): Flow<NetworkResult<AuthRegisterToken>> = handleNetworkCall(
        networkCall = {
            dataSource.newUserVerifyCode(
                authCodeId = authCodeId,
                verifyCodeRequest = VerifyCodeRequest(verifyCode)
            )
        },
        mapToDomain = { it.toDomain }
    )

    override suspend fun refreshToken(): Flow<NetworkResult<AuthToken>> = handleNetworkCall(
        networkCall = {
            dataSource.refreshToken(
                RefreshTokenRequest("todo: JWT DataStore 추가 후 반영")
            )
        },
        mapToDomain = { it.toDomain }
    )

    override suspend fun requestVerification(phoneNumber: String): Flow<NetworkResult<AuthVerifyToken>> =
        handleNetworkCall(
            networkCall = {
                dataSource.requestVerification(
                    sendAuthCodeRequest = SendAuthCodeRequest(phoneNumber = phoneNumber)
                )
            },
            mapToDomain = { it.toDomain }
        )
}