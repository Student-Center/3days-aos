package com.weave.model.domain.user

import com.weave.model.domain.myprofile.JobOccupation
import java.util.UUID

data class UserProfile(
    val gender: Gender,
    val birthYear: Int,
    val jobOccupation: JobOccupation,
    val locationIds: List<UUID>,
    val companyId: UUID? = null
)