package com.weave.network.api

import com.weave.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.google.gson.annotations.SerializedName

import com.weave.network.model.ErrorResponse
import com.weave.network.model.Location

interface LocationsApi {
    /**
     * 지역 목록 조회
     * 시, 도 단위의 주요 행정 구역 목록을 조회합니다.
     * Responses:
     *  - 200: 조회 성공
     *  - 500: 서버 오류
     *
     * @return [kotlin.collections.List<kotlin.String>]
     */
    @GET("locations/regions")
    suspend fun getLocationRegions(): Response<kotlin.collections.List<kotlin.String>>

    /**
     * 지역 목록 조회
     * 시, 도 단위의 주요 지역을 기반으로 지역을 조회합니다.
     * Responses:
     *  - 200: 조회 성공
     *  - 404: 리소스를 찾을 수 없음
     *  - 500: 서버 오류
     *
     * @param regionName 조회할 지역의 시, 도 이름
     * @return [kotlin.collections.List<Location>]
     */
    @GET("locations/{regionName}")
    suspend fun getLocationsByRegion(@Path("regionName") regionName: kotlin.String): Response<kotlin.collections.List<Location>>

}
