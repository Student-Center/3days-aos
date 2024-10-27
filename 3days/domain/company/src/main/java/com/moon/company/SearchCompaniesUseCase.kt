package com.moon.company

import com.weave.domain.repository.CompanyRepository
import com.weave.model.domain.myprofile.Company
import com.weave.model.network.NetworkResult
import com.weave.model.network.PageableResponse
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class SearchCompaniesUseCase @Inject constructor(
    private val repository: CompanyRepository
) {
    suspend operator fun invoke(
        keyword: String,
        next: UUID?,
    ): Flow<NetworkResult<PageableResponse<Company>>> =
        repository.searchCompanies(name = keyword, next = next)
}