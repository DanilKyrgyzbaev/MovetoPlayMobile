package com.movetoplay.domain.model

data class ParentAccount(
    val age: Int,
    val createdAt: String,
    val email: String,
    val id: String,
    val isBanned: Boolean,
    val isConfirmed: Boolean,
    val isGoogleLinked: Boolean,
    val password: String,
    val role: String,
    val updatedAt: String
)