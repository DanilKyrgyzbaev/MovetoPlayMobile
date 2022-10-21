package com.movetoplay.data.model.user_apps

import kotlinx.serialization.Serializable

@Serializable
data class LimitedBody(
    val allowTime: Long?,
    val type: String?
)