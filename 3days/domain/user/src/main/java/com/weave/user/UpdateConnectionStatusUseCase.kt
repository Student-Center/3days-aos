package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.enum.ConnectionStatus
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateConnectionStatusUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(status: ConnectionStatus): Flow<NetworkResult<ConnectionStatus>> = repository.updateConnectionStatus(status)
}