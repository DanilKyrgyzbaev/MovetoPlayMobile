package com.movetoplay.domain.model.user_apps

import kotlinx.serialization.Serializable

@Serializable
data class UserAppsItem(
    val createdAt: String?,
    val id: String?,
    val name: String?,
    val packageName: String?,
    val profileId: String?,
    val updatedAt: String?
)