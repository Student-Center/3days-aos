package com.weave.domain.repository

import com.weave.model.auth.AuthToken
import com.weave.model.auth.AuthVerifyToken
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun authPhonePost(phoneNumber: String): Flow<NetworkResult<Unit>>

    suspend fun authRefreshPost(refreshToken: String): Flow<NetworkResult<AuthToken>>

    suspend fun authRegisterPost(registerToken: String, name: String, email: String): Flow<NetworkResult<AuthToken>>

    suspend fun authVerifyPost(phoneNumber: String, code: String): Flow<NetworkResult<AuthVerifyToken>>
}