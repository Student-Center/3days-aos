package com.weave.intro

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.intro.utils.AuthSmsReceiver
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

sealed class AuthAction : UIAction {
    data class RequestVerificationCode(val phoneNum: String) : AuthAction()
    data class VerifyCode(val inputCode: String) : AuthAction()
    data class AutoFillCode(val code: String) : AuthAction()
}

sealed class AuthIntent : UIIntent {
    data class SendVerificationCodeIntent(val phoneNum: String) : AuthIntent()
    data class ValidateCodeIntent(val code: String) : AuthIntent()
    data class AutoFillCodeIntent(val code: String) : AuthIntent()
}

data class AuthState(
    val code: SnapshotStateList<String> = mutableStateListOf("", "", "", "", "", ""),
    val isVerified: Boolean = false,
    val autoFilledCode: String = "",
    val errorMessage: String = ""
) : UIState

sealed class AuthEffect : UIEffect {
    data class ShowToast(val message: String, val type: SnackBarType) : AuthEffect()
    data object NavigateToNextScreen : AuthEffect()
    data class AutoFillCodeEffect(val code: String) : AuthEffect()
}

@HiltViewModel
class MobileEnterAuthViewModel @Inject constructor(
    private val application: Context
) : BaseViewModel<AuthAction, AuthIntent, AuthState, AuthEffect>(initialState = AuthState()),
    AuthSmsReceiver.OtpReceiveListener {

    // UIAction을 UIIntent로 변환
    override fun actionPredicate(action: AuthAction): AuthIntent {
        return when (action) {
            is AuthAction.RequestVerificationCode -> AuthIntent.SendVerificationCodeIntent(action.phoneNum)
            is AuthAction.VerifyCode -> AuthIntent.ValidateCodeIntent(action.inputCode)
            is AuthAction.AutoFillCode -> AuthIntent.AutoFillCodeIntent(action.code)
        }
    }

    // UIIntent에 따른 로직 처리
    override fun collectIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SendVerificationCodeIntent -> sendVerificationCode(intent.phoneNum)
            is AuthIntent.ValidateCodeIntent -> verifyCode(intent.code)
            is AuthIntent.AutoFillCodeIntent -> autoFillCode(intent.code)
        }
    }

    // 인증번호 자동 채우기
    private fun autoFillCode(code: String) {
        setState { copy(autoFilledCode = code) }
        setEffect { AuthEffect.AutoFillCodeEffect(code) }
    }

    // 인증번호 발송 로직
    private fun sendVerificationCode(phoneNum: String) {
        viewModelScope.launch {
            // 서버로 인증번호 요청을 보내는 API 호출 (예시)
            val result = sendCodeToServer()  // 네트워크 요청 함수
            if (result) {
                setEffect {
                    AuthEffect.ShowToast(
                        application.getString(R.string.mobile_auth_resend_message),
                        SnackBarType.DEFAULT
                    )
                }
            } else {
                setState { copy(errorMessage = application.getString(com.weave.design_system.R.string.mobile_auth_send_error_message)) }
                setEffect {
                    AuthEffect.ShowToast(
                        application.getString(R.string.mobile_auth_send_error_message),
                        SnackBarType.ERROR
                    )
                }
            }
        }
    }

    // 인증번호 검증 로직
    private fun verifyCode(inputCode: String) {
        viewModelScope.launch {
            // 서버에 입력된 인증번호를 보내 검증 (예시)
            val isValid = verifyCodeWithServer(inputCode)  // 네트워크 요청 함수
            if (isValid) {
                setState { copy(isVerified = true) }
                setEffect { AuthEffect.NavigateToNextScreen }
            } else {
                setState { copy(errorMessage = application.getString(com.weave.design_system.R.string.mobile_auth_verify_error_message)) }
                setEffect {
                    AuthEffect.ShowToast(
                        application.getString(R.string.mobile_auth_verify_error_message),
                        SnackBarType.ERROR
                    )
                }
            }
        }
    }

    // 서버 통신 시뮬레이션
    private suspend fun sendCodeToServer(): Boolean {
        return true // 인증번호 발송 성공 가정
    }

    private suspend fun verifyCodeWithServer(code: String): Boolean {
        return code == "123456"  // 인증번호 검증 성공 조건 가정
    }

    private var smsReceiver: AuthSmsReceiver? = null
    private var weakActivity: WeakReference<Activity>? = null

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun startSmsRetriever(activity: Activity) {
        weakActivity = WeakReference(activity)
        SmsRetriever.getClient(activity).startSmsRetriever().also { task ->
            task.addOnSuccessListener {
                if (smsReceiver == null) {
                    smsReceiver = AuthSmsReceiver().apply {
                        setSmsListener(this@MobileEnterAuthViewModel)
                    }
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
                    weakActivity?.get()?.registerReceiver(smsReceiver, smsReceiver!!.doFilter(),
                        Context.RECEIVER_EXPORTED
                    )
                } else {
                    weakActivity?.get()?.registerReceiver(smsReceiver, smsReceiver!!.doFilter())
                }
            }

            task.addOnFailureListener {
                setEffect {
                    AuthEffect.ShowToast(
                        message = it.message.toString(),
                        type = SnackBarType.ERROR
                    )
                }
            }
        }
    }

    override fun onSmsReceived(otp: String) {
        setAction(AuthAction.AutoFillCode(otp))
        stopSmsRetriever()
    }

    fun stopSmsRetriever() {
        weakActivity?.get()?.let { activity ->
            if (smsReceiver != null) {
                activity.unregisterReceiver(smsReceiver)
                smsReceiver = null
            }
        }
    }
}