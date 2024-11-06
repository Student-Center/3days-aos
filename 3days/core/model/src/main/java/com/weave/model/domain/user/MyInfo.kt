package com.weave.model.domain.user

import java.util.UUID

data class MyInfo(
    val id: UUID? = null,
    val name: String,
    val phoneNumber: String,
    val profile: UserProfile,
    val desiredPartner: UserDesiredPartner,
    val profileWidgets: List<ProfileWidget>,
)
