package com.movetoplay.data.model

data class ExerciseResponse(
    val count: Int,
    val createdAt: String,
    val id: String,
    val profileId: String,
    val seconds: Int,
    val type: String,
    val updatedAt: String
)