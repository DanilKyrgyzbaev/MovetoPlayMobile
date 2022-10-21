package com.movetoplay.domain.model

data class User(
    val email: String,
    val password: String,
    val token: String? = null
)
