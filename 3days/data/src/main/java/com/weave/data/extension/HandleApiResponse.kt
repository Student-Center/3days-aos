package com.weave.data.extension

import com.weave.model.network.NetworkError
import com.weave.model.network.NetworkResult

suspend fun <T> handleApiResponse(apiCall: suspend () -> retrofit2.Response<T>): NetworkResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            NetworkResult.Success(response.body()!!)
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error(NetworkError.NULL_RESPONSE_BODY)
        } else {
            NetworkResult.Error(NetworkError.getNetworkErrorByCode(response.code()))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        NetworkResult.Error(NetworkError.UNKNOWN)
    }
}