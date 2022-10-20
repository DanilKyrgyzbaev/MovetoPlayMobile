package com.movetoplay.domain.model

import com.movetoplay.domain.model.Gender

data class CreateProfile(
    val fullName: String,
    val age: Int,
    val sex: Gender,
    val isEnjoySport: Boolean,
)