package com.weave.auth

import com.weave.domain.repository.AuthRepository
import com.weave.model.auth.AuthToken
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class ExistingUserVerifyCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        authCodeId: UUID,
        verifyCode: String
    ): Flow<NetworkResult<AuthToken>> =
        repository.existingUserVerifyCode(authCodeId = authCodeId, verifyCode = verifyCode)
}