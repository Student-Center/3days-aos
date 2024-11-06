package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.auth.AuthToken
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        xRegisterToken: String,
        registerInfo: RegisterInfo
    ): Flow<NetworkResult<AuthToken>> =
        repository.registerUser(
            xRegisterToken = xRegisterToken,
            registerInfo = registerInfo
        )
}