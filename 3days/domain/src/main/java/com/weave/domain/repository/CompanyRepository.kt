package com.weave.domain.repository

import com.weave.model.domain.myprofile.Company
import com.weave.model.network.NetworkResult
import com.weave.model.network.PageableResponse
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CompanyRepository {

    suspend fun searchCompanies(
        name: String,
        next: UUID? = null
    ): Flow<NetworkResult<PageableResponse<Company>>>
}