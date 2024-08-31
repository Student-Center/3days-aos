package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.AuthVerifyPost200Response
import com.weave.network.model.PhoneRequest
import com.weave.network.model.RefreshRequest
import com.weave.network.model.RegisterRequest
import com.weave.network.model.TokenResponse
import com.weave.network.model.VerificationRequest

interface AuthRemoteDataSource {

    suspend fun authPhonePost(phoneRequest: PhoneRequest): NetworkResult<Unit>

    suspend fun authRefreshPost(refreshRequest: RefreshRequest): NetworkResult<TokenResponse>

    suspend fun authRegisterPost(registerRequest: RegisterRequest): NetworkResult<TokenResponse>

    suspend fun authVerifyPost(verificationRequest: VerificationRequest): NetworkResult<AuthVerifyPost200Response>
}