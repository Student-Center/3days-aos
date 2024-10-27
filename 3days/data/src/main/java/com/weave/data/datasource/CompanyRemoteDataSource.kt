package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.SearchCompaniesResponse
import java.util.UUID

interface CompanyRemoteDataSource {

    suspend fun searchCompanies(name: String, next: UUID? = null): NetworkResult<SearchCompaniesResponse>
}