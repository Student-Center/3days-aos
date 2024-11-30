package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.ProfileWidget
import com.weave.network.model.ProfileWidgetType
import com.weave.network.model.UpdateMyUserInfoRequest
import com.weave.network.model.UpdateMyUserInfoResponse
import com.weave.network.model.UpdateUserDesiredPartnerRequest
import com.weave.network.model.UpdateUserDesiredPartnerResponse

interface UserRemoteDataSource {

    suspend fun getMyUserInfo(): NetworkResult<GetMyUserInfoResponse>

    suspend fun updateMyUserInfo(body: UpdateMyUserInfoRequest): NetworkResult<UpdateMyUserInfoResponse>

    suspend fun putProfileWidget(body: ProfileWidget): NetworkResult<ProfileWidget>

    suspend fun deleteProfileWidget(type: ProfileWidgetType): NetworkResult<Unit>

    suspend fun updateMyDesiredPartner(body: UpdateUserDesiredPartnerRequest): NetworkResult<UpdateUserDesiredPartnerResponse>
}