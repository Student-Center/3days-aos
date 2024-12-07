package com.weave.model.domain.user

import java.net.URI
import java.util.UUID

data class ProfileImage(
    val id: UUID,
    val url: URI
)
