package com.weave.model.enum

enum class PreferDistance(val value: String, val koValue: String) {
    ONLY_MY_AREA("ONLY_MY_AREA", "내 활동 지역에서만 받는 걸 선호해요"),
    INCLUDE_SURROUNDING_REGIONS("INCLUDE_SURROUNDING_REGIONS", "내 활동 지역을 포함한 시, 도까지 괜찮아요"),
    ANYWHERE("ANYWHERE", "어디든 괜찮아요");
}