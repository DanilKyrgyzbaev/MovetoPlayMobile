package com.movetoplay.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Child(
    var fullName: String = "",
    var sex: Gender = Gender.MAN,
    var age: Int = 0,
    var isEngagedSports: Boolean = false,
    val id: String = "",
    val parentAccountId: String = ""
)