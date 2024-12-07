package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class DeleteProfileImageUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(imageId: UUID): Flow<NetworkResult<Unit>> = repository.deleteProfileImage(imageId)
}