package com.movetoplay.model

data class Touch(
    val type: String,
    val count: Int,
    val startUnixTimestamp: Int
)
