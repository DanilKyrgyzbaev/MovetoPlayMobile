package com.movetoplay.util

import com.google.gson.GsonBuilder
import io.ktor.util.reflect.*

inline fun <reified T> parseArray(json: String?, typeToken: Type): T? {
    val gson = GsonBuilder().create()
    return gson.fromJson<T>(json, typeToken)
}