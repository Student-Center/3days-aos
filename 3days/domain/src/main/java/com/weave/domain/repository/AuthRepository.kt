package com.weave.domain.repository

import com.weave.model.auth.AuthRegisterToken
import com.weave.model.auth.AuthToken
import com.weave.model.auth.AuthVerifyToken
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface AuthRepository {

    suspend fun existingUserVerifyCode(authCodeId: UUID, verifyCode: String): Flow<NetworkResult<AuthToken>>

    suspend fun newUserVerifyCode(authCodeId: UUID, verifyCode: String): Flow<NetworkResult<AuthRegisterToken>>

    suspend fun refreshToken(): Flow<NetworkResult<AuthToken>>

    suspend fun requestVerification(phoneNumber: String): Flow<NetworkResult<AuthVerifyToken>>
}