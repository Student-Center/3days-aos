package com.weave.data.mapper

import com.weave.model.auth.AuthRegisterToken
import com.weave.model.auth.AuthToken
import com.weave.model.auth.AuthVerifyToken
import com.weave.network.model.AuthVerifyPost200Response
import com.weave.network.model.RegisterTokenResponse
import com.weave.network.model.TokenResponse

val TokenResponse.toDomainModel
    get() = AuthToken(accessToken = accessToken, refreshToken = refreshToken)

val AuthVerifyPost200Response.toDomainModel
    get() = AuthVerifyToken(accessToken = accessToken, refreshToken = refreshToken, registerToken = registerToken)

val RegisterTokenResponse.toDomainModel
    get() = AuthRegisterToken(registerToken = registerToken)