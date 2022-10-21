package com.movetoplay.domain.model.user_apps

data class UserApp(
    val allowTime: Long,
    val createdAt: String,
    val deviceId: String,
    val id: String,
    val name: String,
    val packageName: String,
    val profileId: String,
    var type: String,
    val updatedAt: String
)