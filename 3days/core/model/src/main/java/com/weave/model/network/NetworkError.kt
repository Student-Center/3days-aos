package com.weave.model.network

enum class NetworkError(val errorCode: String, val code: Int) {
    NO_CONNECTION("NO_INTERNET_CONNECTION", 0),
    BAD_REQUEST("BAD_REQUEST", 400),
    UNAUTHORIZED("UNAUTHORIZED_ACCESS", 401),
    NOT_FOUND("RESOURCE_NOT_FOUND", 404),
    TIMEOUT("NETWORK_TIMEOUT", 408),
    SERVER_ERROR("INTERNAL_SERVER_ERROR", 500),
    UNKNOWN("UNKNOWN_ERROR", -1);

    fun from(): String {
        return when (this) {
            NO_CONNECTION -> "인터넷 연결이 없습니다. 네트워크를 확인해 주세요."
            BAD_REQUEST -> "잘못된 요청입니다. 입력을 확인해 주세요."
            UNAUTHORIZED -> "이 리소스에 접근할 권한이 없습니다."
            NOT_FOUND -> "요청한 리소스를 찾을 수 없습니다."
            TIMEOUT -> "요청 시간이 초과되었습니다. 다시 시도해 주세요."
            SERVER_ERROR -> "서버에 오류가 발생했습니다. 나중에 다시 시도해 주세요."
            UNKNOWN -> "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요."
        }
    }

    companion object {
        fun getNetworkErrorByCode(code: Int): NetworkError {
            return NetworkError.entries.find { it.code == code } ?: UNKNOWN
        }
    }
}
