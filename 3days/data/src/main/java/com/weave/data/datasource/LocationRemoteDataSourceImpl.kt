package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.model.network.NetworkResult
import com.weave.network.api.LocationsApi
import com.weave.network.model.Location
import javax.inject.Inject

class LocationRemoteDataSourceImpl @Inject constructor(
    private val service: LocationsApi
) : LocationRemoteDataSource {

    override suspend fun getLocationRegions(): NetworkResult<List<String>> {
        return handleApiResponse {
            service.getLocationRegions()
        }
    }

    override suspend fun getLocationsByRegion(regionName: String): NetworkResult<List<Location>> {
        return handleApiResponse {
            service.getLocationsByRegion(regionName)
        }
    }
}