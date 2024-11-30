package com.weave.home.profile

enum class ProfileEditType(val title: String, val isPartnerType: Boolean) {
    JOB_OCCUPATION("직군 수정", false),
    COMPANY("내 회사 수정", false),
    LOCATION("활동 지역 수정", false),
    PARTNER_AGE("선호 연령", true),
    PARTNER_JOB_OCCUPATION("선호 직군", true),
    PARTNER_DISTANCE("선호 거리", true)
}