package com.weave.model.domain.myprofile

import com.weave.model.domain.user.Gender
import java.util.UUID

data class MyInfoDisplay(
    val gender: Gender,
    val birthYear: Int,
    val jobOccupation: JobOccupation,
    val locations: List<Pair<UUID, String>>,
    val company: Company? = null
)