package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.model.network.NetworkResult
import com.weave.network.api.CompaniesApi
import com.weave.network.model.SearchCompaniesResponse
import java.util.UUID
import javax.inject.Inject

class CompanyRemoteDataSourceImpl @Inject constructor(
    private val service: CompaniesApi
) : CompanyRemoteDataSource {

    override suspend fun searchCompanies(
        name: String,
        next: UUID?
    ): NetworkResult<SearchCompaniesResponse> {
        return handleApiResponse {
            service.searchCompanies(name = name, next = next, limit = 100)
        }
    }
}