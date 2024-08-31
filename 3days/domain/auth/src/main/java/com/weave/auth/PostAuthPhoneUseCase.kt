package com.weave.auth

import com.weave.domain.repository.AuthRepository
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostAuthPhoneUseCase @Inject constructor(
    private val repository: AuthRepository
){
    suspend operator fun invoke(phoneNumber: String): Flow<NetworkResult<Unit>> = repository.authPhonePost(phoneNumber)
}