package com.weave.auth

import com.weave.domain.repository.AuthRepository
import com.weave.model.auth.AuthRegisterToken
import com.weave.model.auth.AuthToken
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Flow<NetworkResult<AuthToken>> =
        repository.refreshToken()
}