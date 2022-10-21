package com.movetoplay.domain.model

data class ChildDevice(
    val createdAt: String,
    val id: String,
    val macAddress: String,
    val name: String,
    val profileId: String,
    val updatedAt: String,
    val profile: Profile?
)