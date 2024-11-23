package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class UpdateMyInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        name: String? = null,
        jobOccupation: JobOccupation? = null,
        companyId: UUID? = null,
        locationIds: List<UUID>? = null
    ): Flow<NetworkResult<Boolean>> = repository.updateMyUserInfo(
        name, jobOccupation, companyId, locationIds
    )
}