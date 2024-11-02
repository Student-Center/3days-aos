package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.Location

interface LocationRemoteDataSource {

    suspend fun getLocationRegions(): NetworkResult<List<String>>

    suspend fun getLocationsByRegion(regionName: String): NetworkResult<List<Location>>
}