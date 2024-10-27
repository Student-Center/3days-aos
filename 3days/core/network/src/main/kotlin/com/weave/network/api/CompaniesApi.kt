package com.weave.network.api

import com.weave.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.google.gson.annotations.SerializedName

import com.weave.network.model.ErrorResponse
import com.weave.network.model.GetCompanyDetailsResponse
import com.weave.network.model.SearchCompaniesResponse

interface CompaniesApi {
    /**
     * 직장 상세 정보 조회
     * 직장 식별자에 해당하는 직장 상세 정보를 조회합니다.
     * Responses:
     *  - 200: 조회 성공
     *  - 404: 리소스를 찾을 수 없음
     *  - 500: 서버 오류
     *
     * @param companyId 조회할 회사의 고유 식별자
     * @return [GetCompanyDetailsResponse]
     */
    @GET("companies/{companyId}")
    suspend fun getCompanyDetails(@Path("companyId") companyId: java.util.UUID): Response<GetCompanyDetailsResponse>

    /**
     * 직장명 검색
     * 직장을 검색합니다.
     * Responses:
     *  - 200: 검색 성공
     *  - 500: 서버 오류
     *
     * @param name 검색할 이름
     * @param next 페이지네이션을 위한 다음 검색 시작 ID (optional)
     * @param limit 반환할 최대 결과 수 (optional, default to 20)
     * @return [SearchCompaniesResponse]
     */
    @GET("companies")
    suspend fun searchCompanies(@Query("name") name: kotlin.String, @Query("next") next: java.util.UUID? = null, @Query("limit") limit: kotlin.Int? = 20): Response<SearchCompaniesResponse>

}
