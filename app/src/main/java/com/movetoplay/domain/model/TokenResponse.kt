package com.movetoplay.domain.model

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)