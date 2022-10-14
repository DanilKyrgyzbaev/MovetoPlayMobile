package com.movetoplay.data.model.user_apps

import kotlinx.serialization.Serializable

@Serializable
data class UserAppsBody(
    val apps: List<AppBody>?
)