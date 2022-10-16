package com.movetoplay.model

data class CreateProfile(
    val fullName: String,
    val age: String,
    val sex: String,
    val isEnjoySport: Boolean,
    val id: String? = null
)