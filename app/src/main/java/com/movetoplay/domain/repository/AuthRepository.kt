package com.movetoplay.domain.repository

import com.movetoplay.domain.utils.RequestStatus

interface AuthRepository {
    suspend fun signIn(email: String, password: String) : RequestStatus
    suspend fun signUp(email: String, password: String) : RequestStatus
}