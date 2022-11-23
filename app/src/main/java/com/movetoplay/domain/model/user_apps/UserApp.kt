package com.movetoplay.domain.model.user_apps

import android.graphics.drawable.Drawable

data class UserApp(
    var allowTime: Long? = null,
    var time:Long? = null,
    val createdAt: String ="",
    val deviceId: String = "",
    val id: String = "",
    val name: String = "",
    val packageName: String = "",
    val profileId: String = "",
    var type: String = "",
    val updatedAt: String = "",
    var drawable: Drawable? = null
)