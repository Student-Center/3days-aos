package com.weave.model.network

import java.util.UUID

data class PageableResponse<T>(
    val next: UUID? = null,
    val data: List<T>
)
