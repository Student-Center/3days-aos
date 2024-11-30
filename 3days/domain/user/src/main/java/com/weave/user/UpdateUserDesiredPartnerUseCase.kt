package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.enum.PreferDistance
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserDesiredPartnerUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        birthYearRange: BirthYearRange,
        jobOccupations: List<JobOccupation>,
        preferDistance: PreferDistance
    ): Flow<NetworkResult<Boolean>> = repository.updateMyDesiredPartner(
        birthYearRange, jobOccupations, preferDistance
    )
}