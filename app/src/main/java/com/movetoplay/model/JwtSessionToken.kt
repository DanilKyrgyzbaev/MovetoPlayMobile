package com.movetoplay.model

data class JwtSessionToken(
    val accountId: String,
    val code: Int,
    val jwtSessionToken: String? = null
)
