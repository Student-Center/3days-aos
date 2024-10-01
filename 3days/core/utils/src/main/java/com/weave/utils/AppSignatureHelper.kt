package com.weave.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class AppSignatureHelper(context: Context?) : ContextWrapper(context) {

    val appSignatures: List<String>
        get() {
            val appCodes = mutableListOf<String>()

            try {
                val packageName = packageName
                val signatures = getPackageSignatures(packageName)

                signatures.forEach { signature ->
                    hashSignature(packageName, signature)?.let { appCodes.add(it) }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, "패키지를 찾을 수 없어 해시를 가져올 수 없습니다.", e)
            }

            return appCodes
        }


    private fun getPackageSignatures(packageName: String): Array<Signature> {
        val packageManager = packageManager
        return packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
    }

    private fun hashSignature(packageName: String, signature: Signature): String? {
        return hash(packageName, signature.toCharsString())
    }

    private fun hash(packageName: String, signature: String): String? {
        val appInfo = "$packageName $signature"
        return try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            val hashSignature = messageDigest.digest().take(NUM_HASHED_BYTES).toByteArray()
            encodeToBase64(hashSignature)
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "해시 알고리즘을 찾을 수 없습니다.", e)
            null
        }
    }

    private fun encodeToBase64(hashSignature: ByteArray): String {
        val base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
        return base64Hash.take(NUM_BASE64_CHAR)
    }

    companion object {
        private val TAG: String = AppSignatureHelper::class.java.simpleName
        private const val HASH_TYPE = "SHA-256" // 해시 알고리즘 타입
        private const val NUM_HASHED_BYTES = 9 // 해시된 바이트 수
        private const val NUM_BASE64_CHAR = 11 // Base64 문자 수
    }
}