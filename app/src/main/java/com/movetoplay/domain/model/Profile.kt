package com.movetoplay.domain.model

data class Profile(
    val age: Int,
    val createdAt: String,
    val fullName: String,
    val id: String,
    val isEnjoySport: Boolean,
    val parentAccountId: String,
    val sex: String,
    val updatedAt: String
)