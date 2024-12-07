package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.model.network.NetworkResult
import com.weave.network.api.UsersApi
import com.weave.network.model.CompleteProfileImageUploadRequest
import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.GetProfileImageUploadUrlResponse
import com.weave.network.model.ProfileImageExtension
import com.weave.network.model.ProfileWidget
import com.weave.network.model.ProfileWidgetType
import com.weave.network.model.UpdateMyUserInfoRequest
import com.weave.network.model.UpdateMyUserInfoResponse
import com.weave.network.model.UpdateUserDesiredPartnerRequest
import com.weave.network.model.UpdateUserDesiredPartnerResponse
import java.util.UUID
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val service: UsersApi,
) : UserRemoteDataSource {

    override suspend fun getMyUserInfo(): NetworkResult<GetMyUserInfoResponse> {
        return handleApiResponse {
            service.getMyUserInfo()
        }
    }

    override suspend fun updateMyUserInfo(body: UpdateMyUserInfoRequest): NetworkResult<UpdateMyUserInfoResponse> {
        return handleApiResponse {
            service.updateMyUserInfo(body)
        }
    }

    override suspend fun putProfileWidget(body: ProfileWidget): NetworkResult<ProfileWidget> {
        return handleApiResponse {
            service.putProfileWidget(body)
        }
    }

    override suspend fun deleteProfileWidget(type: ProfileWidgetType): NetworkResult<Unit> {
        return handleApiResponse {
            service.deleteProfileWidget(type)
        }
    }

    override suspend fun updateMyDesiredPartner(body: UpdateUserDesiredPartnerRequest): NetworkResult<UpdateUserDesiredPartnerResponse> {
        return handleApiResponse {
            service.updateMyDesiredPartner(body)
        }
    }

    override suspend fun getProfileImageUploadUrl(extension: ProfileImageExtension): NetworkResult<GetProfileImageUploadUrlResponse> {
        return handleApiResponse {
            service.getProfileImageUploadUrl(extension)
        }
    }

    override suspend fun completeProfileImageUpload(completeProfileImageUploadRequest: CompleteProfileImageUploadRequest): NetworkResult<Unit> {
        return handleApiResponse {
            service.completeProfileImageUpload(completeProfileImageUploadRequest)
        }
    }

    override suspend fun deleteProfileImage(imageId: UUID): NetworkResult<Unit> {
        return handleApiResponse {
            service.deleteProfileImage(imageId)
        }
    }
}