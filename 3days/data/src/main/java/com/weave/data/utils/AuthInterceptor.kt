package com.weave.data.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.JsonSyntaxException
import com.weave.data.BuildConfig
import com.weave.domain.repository.TokenRepository
import com.weave.network.api.AuthApi
import com.weave.network.infrastructure.Serializer
import com.weave.network.model.ErrorResponse
import com.weave.network.model.RefreshTokenRequest
import com.weave.network.model.TokenResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApi: AuthApi,
    private val tokenRepository: TokenRepository
) : Interceptor {
    private val lock = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val initialRequest = chain.request().addAuthHeaderIfNeeded()
        logRequest(initialRequest)

        var response = chain.proceed(initialRequest)
        logResponse(response)

        if (!response.isSuccessful) {
            response = handleErrorResponse(response, initialRequest, chain)
            logResponse(response, "Retry")
        }

        return response
    }

    private fun logRequest(request: Request) {
        val requestBody = request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } ?: "Empty body"

        logDebug {
            Log.d(
                TAG, """
            ┌─── Request ───────────────────────
            │ URL: ${request.url}
            │ Method: ${request.method}
            │ Headers: ${formatHeaders(request.headers)}
            │ Body: $requestBody
            └───────────────────────────────────
        """.trimIndent()
            )
        }
    }

    private fun logResponse(response: Response, prefix: String = "") {
        val responseBody = response.peekErrorBody() ?: "Empty body"
        val status = if (response.isSuccessful) "Success" else "Error"

        logDebug {
            Log.d(
                TAG, """
            ┌─── Response $prefix [$status] ────────────────
            │ URL: ${response.request.url}
            │ Code: ${response.code}
            │ Message: ${response.message}
            │ Headers: ${formatHeaders(response.headers)}
            │ Body: $responseBody
            └───────────────────────────────────
        """.trimIndent()
            )
        }
    }

    private fun Request.addAuthHeaderIfNeeded(): Request {
        val token = runBlocking { tokenRepository.getTokens().first() }

        return if (token.accessToken.isNotBlank()) {
            addAuthHeader(token.accessToken).also {
                logDebug {
                    Log.d(
                        TAG,
                        "Adding auth header with token: ${token.accessToken.takeLast(10)}..."
                    )
                }
            }
        } else {
            logDebug { Log.d(TAG, "No auth token available") }
            restartApp()
            this
        }
    }

    private fun handleErrorResponse(
        response: Response,
        originalRequest: Request,
        chain: Interceptor.Chain
    ): Response {
        val errorBody = response.peekErrorBody()

        return try {
            val errorResponse = parseErrorResponse(errorBody)
            logDebug {
                Log.d(
                    TAG,
                    "Handling error response: ${errorResponse.code} - ${errorResponse.message}"
                )
            }

            when (errorResponse.code) {
                ERROR_TOKEN_EXPIRED -> {
                    logDebug { Log.d(TAG, "Token expired, attempting refresh") }
                    handleTokenExpiration(response, originalRequest, chain)
                }

                ERROR_TOKEN_INVALID -> {
                    logDebug { Log.d(TAG, "Token invalid, restarting app") }
                    handleInvalidToken(response)
                }

                else -> {
                    logDebug { Log.d(TAG, "Unhandled error code: ${errorResponse.code}") }
                    handleInvalidToken(response)
                }
            }
        } catch (e: JsonSyntaxException) {
            logDebug { Log.e(TAG, "Error parsing error response", e) }
            handleInvalidToken(response)
        }
    }

    private fun Response.peekErrorBody(): String? {
        return try {
            body?.source()?.let { source ->
                source.request(Long.MAX_VALUE)
                source.buffer.clone().readUtf8()
            }
        } catch (e: Exception) {
            logDebug { Log.e(TAG, "Error reading response body", e) }
            null
        }
    }

    private fun parseErrorResponse(errorBody: String?): ErrorResponse {
        return Serializer.gson.fromJson(errorBody, ErrorResponse::class.java)
            ?: throw JsonSyntaxException("Invalid error response")
    }

    private fun handleTokenExpiration(
        response: Response,
        originalRequest: Request,
        chain: Interceptor.Chain
    ): Response {
        return runBlocking {
            lock.withLock {
                val newToken = refreshTokens()
                if (newToken != null) {
                    logDebug { Log.d(TAG, "Token refresh successful, retrying original request") }
                    response.close()
                    val newRequest =
                        originalRequest.recreateRequestWithNewToken(newToken.accessToken)
                    chain.proceed(newRequest)
                } else {
                    logDebug { Log.e(TAG, "Token refresh failed") }
                    handleInvalidToken(response)
                }
            }
        }
    }

    private fun Request.recreateRequestWithNewToken(newToken: String): Request {
        return newBuilder()
            .removeHeader(AUTHORIZATION)
            .build()
            .addAuthHeader(newToken)
    }

    private fun Request.addAuthHeader(token: String): Request {
        return newBuilder()
            .addHeader(AUTHORIZATION, "Bearer $token")
            .build()
    }

    private suspend fun refreshTokens(): TokenResponse? {
        return try {
            val refreshToken = tokenRepository.getTokens().first().refreshToken
            logDebug {
                Log.d(
                    TAG,
                    "Attempting token refresh with refresh token: ${refreshToken.takeLast(10)}..."
                )
            }

            val response = authApi.refreshToken(RefreshTokenRequest(refreshToken))

            response.takeIf { it.isSuccessful }?.body()?.also { tokens ->
                logDebug { Log.d(TAG, "Token refresh successful, saving new tokens") }
                tokenRepository.saveTokens(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken
                )
            }
        } catch (e: Exception) {
            logDebug { Log.e(TAG, "Error refreshing tokens", e) }
            null
        }
    }

    private fun handleInvalidToken(response: Response): Response {
        logDebug { Log.d(TAG, "Handling invalid token, clearing tokens and restarting app") }
        restartApp()
        return response
    }

    private fun restartApp() {
        try {
            clearTokens()
            val intent = createLauncherIntent() ?: run {
                logDebug { Log.e(TAG, "런처 액티비티를 찾을 수 없습니다") }
                return
            }
            restartActivityOrApp(intent)
        } catch (e: Exception) {
            logDebug { Log.e(TAG, "앱 재시작 중 예상치 못한 오류 발생", e) }
        }
    }

    private fun clearTokens() {
        runBlocking {
            withContext(Dispatchers.IO) {
                tokenRepository.clearTokens()
                logDebug { Log.d(TAG, "토큰이 삭제되었습니다") }
            }
        }
    }

    private fun createLauncherIntent(): Intent? {
        return context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_FROM_AUTH_INTERCEPTOR, true)
        }
    }

    private fun restartActivityOrApp(intent: Intent) {
        val currentActivity = context as? Activity
        currentActivity?.runOnUiThread {
            restartCurrentActivity(intent, currentActivity)
        } ?: run {
            restartFromApplicationContext(intent)
        }
    }

    private fun restartCurrentActivity(intent: Intent, currentActivity: Activity) {
        try {
            context.startActivity(intent)
            currentActivity.finish()
            logDebug { Log.d(TAG, "런처 액티비티부터 앱을 재시작합니다") }
        } catch (e: Exception) {
            logDebug { Log.e(TAG, "액티비티 재시작 중 오류 발생: ${e.message}", e) }
        }
    }

    private fun restartFromApplicationContext(intent: Intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            logDebug { Log.d(TAG, "Application Context로 앱을 재시작합니다") }
        } catch (e: Exception) {
            logDebug { Log.e(TAG, "Application Context로 재시작 중 오류 발생: ${e.message}", e) }
        }
    }

    private inline fun logDebug(logStatement: () -> Unit) {
        if (BuildConfig.DEBUG) {
            logStatement()
        }
    }

    private fun formatHeaders(headers: okhttp3.Headers): String {
        return headers.toMap()
            .map { (key, value) ->
                when (key.lowercase()) {
                    "authorization" -> "$key: Bearer ${value.takeLast(10)}..."
                    else -> "$key: ${value.first()}"
                }
            }
            .joinToString(", ", "{", "}")
    }

    companion object {
        private const val TAG = "AuthInterceptor"
        private const val AUTHORIZATION = "Authorization"
        private const val ERROR_TOKEN_EXPIRED = "1006"
        private const val ERROR_TOKEN_INVALID = "1008"
        private const val EXTRA_FROM_AUTH_INTERCEPTOR = "from_auth_interceptor"
    }
}