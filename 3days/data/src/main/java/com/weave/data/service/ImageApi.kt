package com.weave.data.service

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Url

interface ImageApi {

    @PUT
    suspend fun uploadProfileImage(
        @Url uploadUrl: String,
        @Header("x-amz-acl") acl: String = "public-read",
        @Body file: RequestBody
    ): Response<ResponseBody>
}