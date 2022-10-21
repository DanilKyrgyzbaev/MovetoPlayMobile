package com.movetoplay.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NewProfileChildBody(
    val fullName: String,
    val age: Int,
    val sex: String,
    val isEnjoySport: Boolean
)