package com.weave.data.repository

import com.weave.data.datasource.LocationRemoteDataSource
import com.weave.data.extension.handleNetworkCall
import com.weave.data.mapper.toDomain
import com.weave.domain.repository.LocationRepository
import com.weave.model.domain.myprofile.Location
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val dataSource: LocationRemoteDataSource
) : LocationRepository {

    override suspend fun getLocationRegions(): Flow<NetworkResult<List<String>>> =
        handleNetworkCall(
            networkCall = {
                dataSource.getLocationRegions()
            },
            mapToDomain = { it.map { regionName -> regionName } }
        )

    override suspend fun getLocationsByRegion(regionName: String): Flow<NetworkResult<List<Location>>> =
        handleNetworkCall(
            networkCall = {
                dataSource.getLocationsByRegion(regionName = regionName)
            },
            mapToDomain = { it.map { data -> data.toDomain } }
        )
}