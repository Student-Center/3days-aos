package com.weave.location

import com.weave.domain.repository.LocationRepository
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationRegionsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(): Flow<NetworkResult<List<String>>> =
        repository.getLocationRegions()
}