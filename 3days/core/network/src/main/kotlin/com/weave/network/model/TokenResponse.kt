/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.weave.network.model


import com.google.gson.annotations.SerializedName

/**
 * 토큰 발급 응답
 *
 * @param accessToken 액세스 토큰
 * @param refreshToken 리프레시 토큰
 * @param expiresIn 액세스 토큰의 유효 기간 (초 단위)
 */


data class TokenResponse (

    /* 액세스 토큰 */
    @SerializedName("accessToken")
    val accessToken: kotlin.String? = null,

    /* 리프레시 토큰 */
    @SerializedName("refreshToken")
    val refreshToken: kotlin.String? = null,

    /* 액세스 토큰의 유효 기간 (초 단위) */
    @SerializedName("expiresIn")
    val expiresIn: kotlin.Int? = null

) {


}

