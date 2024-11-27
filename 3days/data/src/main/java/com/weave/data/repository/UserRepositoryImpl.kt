package com.weave.data.repository

import com.weave.data.datasource.RegisterRemoteDataSource
import com.weave.data.datasource.TokenLocalDataSource
import com.weave.data.datasource.UserRemoteDataSource
import com.weave.data.extension.handleNetworkCall
import com.weave.data.mapper.toDTO
import com.weave.data.mapper.toDomain
import com.weave.domain.repository.UserRepository
import com.weave.model.auth.AuthToken
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.MyInfo
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.network.NetworkResult
import com.weave.network.model.UpdateMyUserInfoRequest
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserRemoteDataSource,
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
}