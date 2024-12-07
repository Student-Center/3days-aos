package com.weave.data.repository

import com.weave.data.datasource.ImageRemoteDataSource
import com.weave.data.datasource.RegisterRemoteDataSource
import com.weave.data.datasource.TokenLocalDataSource
import com.weave.data.datasource.UserRemoteDataSource
import com.weave.data.extension.handleNetworkCall
import com.weave.data.mapper.toDTO
import com.weave.data.mapper.toDomain
import com.weave.data.utils.FileUtils.toRequestBody
import com.weave.domain.repository.UserRepository
import com.weave.model.auth.AuthToken
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.enum.PreferDistance
import com.weave.model.network.NetworkResult
import com.weave.network.model.CompleteProfileImageUploadRequest
import com.weave.network.model.ProfileImageExtension
import com.weave.network.model.UpdateMyUserInfoRequest
import com.weave.network.model.UpdateUserDesiredPartnerRequest
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserRemoteDataSource,
    private val imageDataSource: ImageRemoteDataSource,
    private val registerDataSource: RegisterRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource
) : UserRepository {

    override suspend fun getMyUserInfo(): Flow<NetworkResult<MyInfo>> = handleNetworkCall(
        networkCall = {
            dataSource.getMyUserInfo()
        },
        mapToDomain = { it.toDomain }
    )

    override suspend fun registerUser(
        xRegisterToken: String,
        registerInfo: RegisterInfo,
    ): Flow<NetworkResult<AuthToken>> = handleNetworkCall(
        networkCall = {
            registerDataSource.registerUser(
                xRegisterToken = xRegisterToken,
                registerUserRequest = registerInfo.toDTO
            )
        },
        mapToDomain = {
            tokenLocalDataSource.saveTokens(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken
            )

            it.toDomain
        }
    )

    override suspend fun updateMyUserInfo(
        name: String,
        jobOccupation: JobOccupation,
        locationIds: List<UUID>,
        companyId: UUID?,
        allowSameCompany: Boolean?
    ): Flow<NetworkResult<Boolean>> = handleNetworkCall(
        networkCall = {
            dataSource.updateMyUserInfo(
                UpdateMyUserInfoRequest(
                    name = name,
                    jobOccupation = jobOccupation.toDTO,
                    locationIds = locationIds,
                    companyId = companyId,
                    allowSameCompany = allowSameCompany
                )
            )
        },
        mapToDomain = {
            true
        }
    )

    override suspend fun putProfileWidget(type: ProfileWidget): Flow<NetworkResult<ProfileWidget>> =
        handleNetworkCall(
            networkCall = {
                dataSource.putProfileWidget(type.toDTO)
            },
            mapToDomain = { it.toDomain }
        )

    override suspend fun deleteProfileWidget(type: ProfileWidgetType): Flow<NetworkResult<Unit>> =
        handleNetworkCall(
            networkCall = { dataSource.deleteProfileWidget(type.toDTO) },
            mapToDomain = { }
        )

    override suspend fun updateMyDesiredPartner(
        birthYearRange: BirthYearRange,
        jobOccupations: List<JobOccupation>,
        preferDistance: PreferDistance
    ): Flow<NetworkResult<Boolean>> = handleNetworkCall(
        networkCall = {
            dataSource.updateMyDesiredPartner(
                UpdateUserDesiredPartnerRequest(
                    birthYearRange = birthYearRange.toDTO,
                    jobOccupations = jobOccupations.map { it.toDTO },
                    preferDistance = preferDistance.toDTO
                )
            )
        },
        mapToDomain = { true }
    )

    override suspend fun getProfileImageUploadUrl(): Flow<NetworkResult<Pair<UUID, String>>> =
        handleNetworkCall(
            networkCall = {
                dataSource.getProfileImageUploadUrl(ProfileImageExtension.PNG)
            },
            mapToDomain = {
                it.toDomain
            }
        )

    override suspend fun completeProfileImageUpload(imageId: UUID): Flow<NetworkResult<Unit>> =
        handleNetworkCall(
            networkCall = {
                dataSource.completeProfileImageUpload(
                    CompleteProfileImageUploadRequest(imageId, ProfileImageExtension.PNG)
                )
            },
            mapToDomain = {}
        )

    override suspend fun uploadProfileImage(
        uploadUrl: String,
        file: File
    ): Flow<NetworkResult<Unit>> = handleNetworkCall(
        networkCall = {
            imageDataSource.uploadProfileImage(uploadUrl, file.toRequestBody())
        },
        mapToDomain = {}
    )

    override suspend fun deleteProfileImage(imageId: UUID): Flow<NetworkResult<Unit>> =
        handleNetworkCall(
            networkCall = { dataSource.deleteProfileImage(imageId) },
            mapToDomain = {}
        )
}