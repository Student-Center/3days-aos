package com.weave.domain.repository

import com.weave.model.auth.AuthToken
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface UserRepository {

    suspend fun getMyUserInfo(): Flow<NetworkResult<MyInfo>>

    suspend fun registerUser(
        xRegisterToken: String,
        registerInfo: RegisterInfo
    ): Flow<NetworkResult<AuthToken>>

    suspend fun updateMyUserInfo(
        name: String,
        jobOccupation: JobOccupation,
        locationIds: List<UUID>,
        companyId: UUID? = null,
        allowSameCompany: Boolean? = null
    ): Flow<NetworkResult<Boolean>>

    suspend fun putProfileWidget(type: ProfileWidget): Flow<NetworkResult<ProfileWidget>>

    suspend fun deleteProfileWidget(type: ProfileWidgetType): Flow<NetworkResult<Unit>>
}