package com.weave.data.repository

import com.weave.data.datasource.CompanyRemoteDataSource
import com.weave.data.extension.handleNetworkCall
import com.weave.data.mapper.toDomain
import com.weave.domain.repository.CompanyRepository
import com.weave.model.domain.myprofile.Company
import com.weave.model.network.NetworkResult
import com.weave.model.network.PageableResponse
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val dataSource: CompanyRemoteDataSource
) : CompanyRepository {

    override suspend fun searchCompanies(
        name: String,
        next: UUID?
    ): Flow<NetworkResult<PageableResponse<Company>>> = handleNetworkCall(
        networkCall = {
            dataSource.searchCompanies(
                name = name,
                next = next
            )
        },
        mapToDomain = { it.toDomain }
    )
}