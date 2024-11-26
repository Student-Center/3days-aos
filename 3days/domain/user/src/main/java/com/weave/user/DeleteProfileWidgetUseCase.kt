package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteProfileWidgetUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(type: ProfileWidgetType): Flow<NetworkResult<Unit>> {
        return repository.deleteProfileWidget(type)
    }
}