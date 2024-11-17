package com.weave.model.domain.user

import com.weave.model.domain.myprofile.MyInfoDisplay
import java.util.UUID

data class MyInfo(
    val id: UUID? = null,
    val name: String,
    val phoneNumber: String,
    val profile: MyInfoDisplay,
    val desiredPartner: UserDesiredPartner,
    val profileWidgets: List<ProfileWidget>,
)
