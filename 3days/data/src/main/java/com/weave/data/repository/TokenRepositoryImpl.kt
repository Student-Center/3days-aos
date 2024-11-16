package com.weave.data.repository

import com.weave.data.datasource.AuthRemoteDataSource
import com.weave.data.datasource.TokenLocalDataSource
import com.weave.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : TokenRepository {
    override suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit> {
        return tokenLocalDataSource.saveTokens(accessToken, refreshToken)
    }

    override fun getTokens() = tokenLocalDataSource.getTokens()

    override suspend fun clearTokens(): Result<Unit> {
        return tokenLocalDataSource.clearTokens()
    }
}