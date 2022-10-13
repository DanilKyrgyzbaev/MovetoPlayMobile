package com.movetoplay.model

data class User(
    val email: String,
    val password: String,
    val token: String? = null
)