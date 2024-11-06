package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.auth.AuthToken
import com.weave.model.domain.user.UserDesiredPartner
import com.weave.model.domain.user.UserProfile
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        xRegisterToken: String,
        name: String,
        phoneNumber: String,
        profile: UserProfile,
        desiredPartner: UserDesiredPartner
    ): Flow<NetworkResult<AuthToken>> =
        repository.registerUser(
            xRegisterToken = xRegisterToken,
            name = name,
            phoneNumber = phoneNumber,
            profile = profile,
            desiredPartner = desiredPartner
        )
}