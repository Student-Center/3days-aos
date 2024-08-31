package com.weave.model.auth

data class AuthVerifyToken(
    val accessToken: String,
    val refreshToken: String,
    val registerToken: String
)
