package com.weave.auth

import com.weave.domain.repository.TokenRepository
import com.weave.model.auth.AuthToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTokensUseCase @Inject constructor(
    private val repository: TokenRepository
) {
    suspend operator fun invoke(): AuthToken = repository.getTokens().first()
}