package com.movetoplay.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegBody(
    val email: String,
    val password : String,
    val age: Int
)