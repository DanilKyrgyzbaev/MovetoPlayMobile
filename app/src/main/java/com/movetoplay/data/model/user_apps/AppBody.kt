package com.movetoplay.data.model.user_apps

import kotlinx.serialization.Serializable

@Serializable
data class AppBody(
    val name: String?,
    val packageName: String?
)