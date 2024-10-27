package com.weave.data.extension

import com.weave.model.network.NetworkError
import com.weave.model.network.NetworkResult

suspend fun <T> handleApiResponse(apiCall: suspend () -> retrofit2.Response<T>): NetworkResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            NetworkResult.Success(response.body()!!)
        } else {
            NetworkResult.Error(NetworkError.getNetworkErrorByCode(response.code()))
        }
    } catch (e: Exception) {
        NetworkResult.Error(NetworkError.UNKNOWN)
    }
}