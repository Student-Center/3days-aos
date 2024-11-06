package com.weave.model.domain.user

import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.enum.PreferDistance

data class UserDesiredPartner(
    val birthYearRange: BirthYearRange,
    val jobOccupations: List<JobOccupation>,
    val preferDistance: PreferDistance,
    val allowSameCompany: Boolean? = null
)