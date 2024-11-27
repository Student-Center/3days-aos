package com.weave.home.profile

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserInfoType : NavType<UserInfo>(isNullableAllowed = false) {
    private val gson = Gson()
    private val type = object : TypeToken<UserInfo>() {}.type

    override fun get(bundle: Bundle, key: String): UserInfo? {
        return bundle.getString(key)?.let { parseValue(it) }
    }

    override fun parseValue(value: String): UserInfo {
        return gson.fromJson(value, type)
    }

    override fun put(bundle: Bundle, key: String, value: UserInfo) {
        bundle.putString(key, gson.toJson(value))
    }
}