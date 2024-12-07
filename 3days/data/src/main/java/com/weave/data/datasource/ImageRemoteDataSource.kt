package com.weave.data.datasource

import com.weave.model.network.NetworkResult
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface ImageRemoteDataSource {

    suspend fun uploadProfileImage(
        uploadUrl: String,
        file: RequestBody
    ): NetworkResult<ResponseBody>

}