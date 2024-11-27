package com.weave.home.profile

import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import java.util.UUID

data class UserInfo(
    val name: String,
    val jobOccupation: JobOccupation,
    val locations: List<Pair<UUID, String>>,
    val company: Company? = null,
    val allowSameCompany: Boolean? = null
)