package com.weave.data.mapper

import com.weave.model.domain.myprofile.Company
import com.weave.model.network.PageableResponse
import com.weave.network.model.SearchCompaniesResponse

val SearchCompaniesResponse.toDomain
    get() = PageableResponse(
        next = next,
        data = companies.map { Company(id = it.id, name = it.name) }
    )