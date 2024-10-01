package com.weave.intro.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.gms.common.internal.service.Common

/**
 * SMS의 규칙
 * 1. SMS 내용은 140byte 이하여야 합니다.
 * 2. SMS 맨 앞은 <#>라는 Text로 시작해야 합니다.
 * 3. SMS 맨 마지막은 앱을 식별하는 11글자 해시 문자열로 끝나야 합니다.
 * ex) <#> [앱 이름] 인증번호는 [1234] 입니다. 0QaSrUKkEGq
 */
class AuthSmsReceiver : BroadcastReceiver() {

    private var otpReceiver: OtpReceiveListener? = null

    interface OtpReceiveListener {
        fun onSmsReceived(otp: String)
    }

    private fun extractVerificationCode(message: String): String? {
        if (message.contains("3days")) {
            val regex = """\d{6}""".toRegex()
            return regex.find(message)?.value
        }
        return null
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            intent.extras?.let { bundle ->
                val status = bundle.get(SmsRetriever.EXTRA_STATUS) as Status

                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val otpSms = bundle.getString(SmsRetriever.EXTRA_SMS_MESSAGE, "")

                        if (otpReceiver != null && otpSms.isNotEmpty()) {
                            val otp = extractVerificationCode(otpSms)
                            if (!otp.isNullOrEmpty()) {
                                otpReceiver!!.onSmsReceived(otp)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setSmsListener(receiver: OtpReceiveListener) {
        this.otpReceiver = receiver
    }

    fun doFilter() = IntentFilter().apply {
        addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
    }
}