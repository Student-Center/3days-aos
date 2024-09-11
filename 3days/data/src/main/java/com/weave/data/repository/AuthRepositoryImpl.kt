package com.weave.data.repository

import com.weave.data.datasource.AuthRemoteDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthRemoteDataSource
) {

//    override suspend fun authPhonePost(phoneNumber: String): Flow<NetworkResult<Unit>> = flow {
//        emit(NetworkResult.Loading)
//        Log.d("AuthRepositoryImpl", "authPhonePost called with $phoneNumber")
//        when (val result = dataSource.authPhonePost(PhoneRequest(phoneNumber))) {
//            is NetworkResult.Success -> {
//                Log.d("AuthRepositoryImpl", "Success: ${result.data}")
//                emit(NetworkResult.Success(result.data))
//            }
//            is NetworkResult.Error -> {
//                Log.d("AuthRepositoryImpl", "Error: ${result.error}")
//                emit(NetworkResult.Error(result.error))
//            }
//            NetworkResult.Loading -> {
//                Log.d("AuthRepositoryImpl", "Loading")
//            }
//        }
//    }
//
//    override suspend fun authRefreshPost(refreshToken: String): Flow<NetworkResult<AuthToken>> =
//        flow {
//            emit(NetworkResult.Loading)
//            when (val result = dataSource.authRefreshPost(RefreshRequest(refreshToken))) {
//                is NetworkResult.Success -> emit(NetworkResult.Success(result.data.toDomainModel))
//                is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
//                NetworkResult.Loading -> {}
//            }
//        }
//
//    override suspend fun authRegisterPost(
//        registerToken: String,
//        name: String,
//        email: String
//    ): Flow<NetworkResult<AuthToken>> = flow {
//        emit(NetworkResult.Loading)
//        when (val result = dataSource.authRegisterPost(
//            RegisterRequest(
//                registerToken = registerToken,
//                profile = Profile(name = name, email = email)
//            )
//        )) {
//            is NetworkResult.Success -> emit(NetworkResult.Success(result.data.toDomainModel))
//            is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
//            NetworkResult.Loading -> {}
//        }
//    }
//
//
//    override suspend fun authVerifyPost(
//        phoneNumber: String,
//        code: String
//    ): Flow<NetworkResult<AuthVerifyToken>> =
//        flow {
//            emit(NetworkResult.Loading)
//            when (val result =
//                dataSource.authVerifyPost(VerificationRequest(phone = phoneNumber, code = code))) {
//                is NetworkResult.Success -> emit(NetworkResult.Success(result.data.toDomainModel))
//                is NetworkResult.Error -> emit(NetworkResult.Error(result.error))
//                NetworkResult.Loading -> {}
//            }
//        }
}