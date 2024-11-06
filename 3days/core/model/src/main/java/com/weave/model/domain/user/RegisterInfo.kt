package com.weave.model.domain.user

data class RegisterInfo(
    val name: String,
    val phoneNumber: String,
    val profile: UserProfile,
    val desiredPartner: UserDesiredPartner
)