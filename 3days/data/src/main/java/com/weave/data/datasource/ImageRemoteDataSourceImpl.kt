package com.weave.data.datasource

import com.weave.data.extension.handleApiResponse
import com.weave.data.service.ImageApi
import com.weave.model.network.NetworkResult
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class ImageRemoteDataSourceImpl @Inject constructor(
    private val service: ImageApi
) : ImageRemoteDataSource {

    override suspend fun uploadProfileImage(
        uploadUrl: String,
        file: RequestBody
    ): NetworkResult<ResponseBody> {
        return handleApiResponse {
            service.uploadProfileImage(uploadUrl = uploadUrl, file = file)
        }
    }
}