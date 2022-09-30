package com.movetoplay.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthBody(
    val email: String,
    val password : String
)