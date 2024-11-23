package com.weave.home.profile.location

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class LocationListType : NavType<List<Pair<UUID, String>>>(isNullableAllowed = false) {
    private val gson = Gson()
    private val listType = object : TypeToken<List<Pair<UUID, String>>>() {}.type

    override fun get(bundle: Bundle, key: String): List<Pair<UUID, String>>? {
        return bundle.getString(key)?.let { parseValue(it) }
    }

    override fun parseValue(value: String): List<Pair<UUID, String>> {
        return gson.fromJson<List<Pair<String, String>>>(value, listType).map { (first, second) ->
            Pair(UUID.fromString(first), second)
        }
    }

    override fun put(bundle: Bundle, key: String, value: List<Pair<UUID, String>>) {
        bundle.putString(key, gson.toJson(value))
    }
}