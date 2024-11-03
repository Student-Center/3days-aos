package com.weave.domain.repository

import com.weave.model.domain.myprofile.Location
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getLocationRegions(): Flow<NetworkResult<List<String>>>

    suspend fun getLocationsByRegion(regionName: String): Flow<NetworkResult<List<Location>>>
}