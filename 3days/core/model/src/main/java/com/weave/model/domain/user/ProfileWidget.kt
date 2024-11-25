package com.weave.model.domain.user

data class ProfileWidget(
    val type: ProfileWidgetType,
    var content: String
)