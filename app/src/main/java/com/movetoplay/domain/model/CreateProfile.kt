package com.movetoplay.domain.model

data class CreateProfile(
    val fullName: String,
    val age: Int,
    val sex: String,
    val isEnjoySport: Boolean,
)