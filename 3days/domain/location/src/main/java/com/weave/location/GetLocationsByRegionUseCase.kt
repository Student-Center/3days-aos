package com.weave.location

import com.weave.domain.repository.LocationRepository
import com.weave.model.domain.myprofile.Location
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationsByRegionUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(regionName: String): Flow<NetworkResult<List<Location>>> =
        repository.getLocationsByRegion(regionName)
}