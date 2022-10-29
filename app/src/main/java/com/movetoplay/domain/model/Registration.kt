package com.movetoplay.domain.model

data class Registration(
    val email: String,
    val password: String,
    val age: Int,
    var accessToken: String? = null,
    var refreshToken: String? = null
)