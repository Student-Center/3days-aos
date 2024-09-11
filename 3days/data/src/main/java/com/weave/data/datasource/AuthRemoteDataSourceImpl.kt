package com.weave.data.datasource

import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
//    private val service: AuthenticationApi
): AuthRemoteDataSource {

//    override suspend fun authPhonePost(phoneRequest: PhoneRequest): NetworkResult<Unit> {
//        return try {
//            val response = service.authPhonePost(phoneRequest)
//            if(response.isSuccessful){
//                NetworkResult.Success(Unit)
//            } else {
//                NetworkResult.Error(NetworkError.UNKNOWN)
//            }
//        } catch (e: Exception) {
//            NetworkResult.Error(NetworkError.UNKNOWN)
//        }
//    }
//
//    override suspend fun authRefreshPost(refreshRequest: RefreshRequest): NetworkResult<TokenResponse> {
//        return try {
//            val response = service.authRefreshPost(refreshRequest)
//            if(response.isSuccessful){
//                NetworkResult.Success(response.body()!!)
//            } else {
//                NetworkResult.Error(NetworkError.UNKNOWN)
//            }
//        } catch (e: Exception) {
//            NetworkResult.Error(NetworkError.UNKNOWN)
//        }
//    }
//
//    override suspend fun authRegisterPost(registerRequest: RegisterRequest): NetworkResult<TokenResponse> {
//        return try {
//            val response = service.authRegisterPost(registerRequest)
//            if(response.isSuccessful){
//                NetworkResult.Success(response.body()!!)
//            } else {
//                NetworkResult.Error(NetworkError.UNKNOWN)
//            }
//        } catch (e: Exception) {
//            NetworkResult.Error(NetworkError.UNKNOWN)
//        }
//    }
//
//    override suspend fun authVerifyPost(verificationRequest: VerificationRequest): NetworkResult<AuthVerifyPost200Response> {
//        return try {
//            val response = service.authVerifyPost(verificationRequest)
//            if(response.isSuccessful){
//                NetworkResult.Success(response.body()!!)
//            } else {
//                NetworkResult.Error(NetworkError.UNKNOWN)
//            }
//        } catch (e: Exception) {
//            NetworkResult.Error(NetworkError.UNKNOWN)
//        }
//    }
}