package com.weave.model.auth

data class AuthVerifyToken(
    val authCodeId: java.util.UUID,
    val userStatus: String? = null
)
