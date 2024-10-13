package com.weave.data.mapper

import com.weave.model.auth.AuthRegisterToken
import com.weave.model.auth.AuthToken
import com.weave.model.auth.AuthVerifyToken
import com.weave.network.model.ExistingUserVerifyCodeResponse
import com.weave.network.model.NewUserVerifyCodeResponse
import com.weave.network.model.SendAuthCodeResponse
import com.weave.network.model.TokenResponse

val ExistingUserVerifyCodeResponse.toDomain
    get() = AuthToken(accessToken = accessToken, refreshToken = refreshToken)

val NewUserVerifyCodeResponse.toDomain
    get() = AuthRegisterToken(registerToken = registerToken, expiresIn = expiresIn ?: 0)

val TokenResponse.toDomain
    get() = AuthToken(accessToken = accessToken, refreshToken = refreshToken)

val SendAuthCodeResponse.toDomain
    get() = AuthVerifyToken(authCodeId = authCodeId, userStatus = userStatus.toString())