package com.movetoplay.domain.repository


interface AuthRepository {
    suspend fun signIn(email: String, password: String) : String
    suspend fun signUp(email: String, password: String, age: Byte) : String
}