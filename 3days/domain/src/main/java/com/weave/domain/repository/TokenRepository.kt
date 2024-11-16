package com.weave.domain.repository

import com.weave.model.auth.AuthToken
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit>

    fun getTokens(): Flow<AuthToken>

    suspend fun clearTokens(): Result<Unit>
}