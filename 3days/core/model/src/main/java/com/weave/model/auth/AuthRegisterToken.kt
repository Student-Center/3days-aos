package com.weave.model.auth

data class AuthRegisterToken(
    val registerToken: String,
    val expiresIn: Int
)
