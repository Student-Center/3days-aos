package com.weave.data.extension

import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T, R> handleNetworkCall(
    networkCall: suspend () -> NetworkResult<T>,
    mapToDomain: suspend (T) -> R
): Flow<NetworkResult<R>> = flow {
    emit(NetworkResult.Loading)
    when (val result = networkCall()) {
        is NetworkResult.Success -> emit(NetworkResult.Success(mapToDomain(result.data)))
        is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
        NetworkResult.Loading -> {}
    }
}