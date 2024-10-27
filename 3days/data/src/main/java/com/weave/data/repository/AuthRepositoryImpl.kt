package com.weave.data.repository

import com.weave.data.datasource.AuthRemoteDataSource
import com.weave.data.mapper.mapToDomain
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
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun existingUserVerifyCode(
        authCodeId: UUID,
        verifyCode: String
    ): Flow<NetworkResult<AuthToken>> = flow {
        emit(NetworkResult.Loading)
        when (val result = dataSource.existingUserVerifyCode(
            authCodeId = authCodeId,
            verifyCodeRequest = VerifyCodeRequest(verifyCode)
        )) {
            is NetworkResult.Success -> emit(NetworkResult.Success(result.data.toDomain))
            is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
            NetworkResult.Loading -> {}
        }
    }

    override suspend fun newUserVerifyCode(
        authCodeId: UUID,
        verifyCode: String
    ): Flow<NetworkResult<AuthRegisterToken>> = flow {
        emit(NetworkResult.Loading)
        when (val result = dataSource.newUserVerifyCode(
            authCodeId = authCodeId,
            verifyCodeRequest = VerifyCodeRequest(verifyCode)
        )) {
            is NetworkResult.Success -> emit(NetworkResult.Success(result.data.toDomain))
            is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
            NetworkResult.Loading -> {}
        }
    }

    override suspend fun refreshToken(): Flow<NetworkResult<AuthToken>> = flow {
        emit(NetworkResult.Loading)
        when (val result = dataSource.refreshToken(
            RefreshTokenRequest("todo: JWT DataStore 추가 후 반영")
        )) {
            is NetworkResult.Success -> emit(NetworkResult.Success(result.data.toDomain))
            is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
            NetworkResult.Loading -> {}
        }
    }

    override suspend fun requestVerification(phoneNumber: String): Flow<NetworkResult<AuthVerifyToken>> = flow {
        emit(NetworkResult.Loading)
        when (val result = dataSource.requestVerification(
            sendAuthCodeRequest = SendAuthCodeRequest(phoneNumber = phoneNumber)
        )) {
            is NetworkResult.Success -> emit(NetworkResult.Success(result.data.toDomain))
            is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
            NetworkResult.Loading -> {}
        }
    }
}