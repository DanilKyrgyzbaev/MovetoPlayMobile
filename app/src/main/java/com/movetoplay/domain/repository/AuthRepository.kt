package com.movetoplay.domain.repository

import com.movetoplay.domain.model.TokenResponse
import com.movetoplay.domain.utils.ResultStatus


interface AuthRepository {
    suspend fun signIn(email: String, password: String): String
    suspend fun signUp(email: String, password: String, age: Byte): String
    suspend fun authorizeProfile(childId: String, deviceId: String): ResultStatus<TokenResponse>
}