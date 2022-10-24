package com.movetoplay.domain.model

data class Exercise(
    val type: TypeExercise,
    val count: Int,
    val max: Int? = null
)