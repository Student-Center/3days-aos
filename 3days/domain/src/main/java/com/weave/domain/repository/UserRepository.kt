package com.weave.domain.repository

import com.weave.model.auth.AuthToken
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.enum.ConnectionStatus
import com.weave.model.enum.PreferDistance
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.io.File
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

    suspend fun updateMyDesiredPartner(
        birthYearRange: BirthYearRange,
        jobOccupations: List<JobOccupation>,
        preferDistance: PreferDistance
    ): Flow<NetworkResult<Boolean>>

    suspend fun getProfileImageUploadUrl(): Flow<NetworkResult<Pair<UUID, String>>>

    suspend fun completeProfileImageUpload(imageId: UUID): Flow<NetworkResult<Unit>>

    suspend fun uploadProfileImage(uploadUrl: String, file: File): Flow<NetworkResult<Unit>>

    suspend fun deleteProfileImage(imageId: UUID): Flow<NetworkResult<Unit>>

    suspend fun updateConnectionStatus(status: ConnectionStatus): Flow<NetworkResult<ConnectionStatus>>

    suspend fun deleteMyUser(): Flow<NetworkResult<Unit>>
}