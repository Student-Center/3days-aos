package com.weave.domain.repository

import com.weave.model.auth.AuthToken
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.UserDesiredPartner
import com.weave.model.domain.user.UserProfile
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getMyUserInfo(): Flow<NetworkResult<MyInfo>>

    suspend fun putProfileWidget(type: ProfileWidget): Flow<NetworkResult<ProfileWidget>>

    suspend fun registerUser(
        xRegisterToken: String,
        name: String,
        phoneNumber: String,
        profile: UserProfile,
        desiredPartner: UserDesiredPartner
    ): Flow<NetworkResult<AuthToken>>
}