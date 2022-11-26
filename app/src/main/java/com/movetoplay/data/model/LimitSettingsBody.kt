package com.movetoplay.data.model

data class LimitSettingsBody(
    val needJumpCount: Int,
    val needSeconds: Int,
    val needSquatsCount: Int,
    val needSqueezingCount: Int,
    val extraTime:Int
)