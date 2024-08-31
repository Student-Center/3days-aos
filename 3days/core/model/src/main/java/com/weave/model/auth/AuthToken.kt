package com.weave.model.auth

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)
