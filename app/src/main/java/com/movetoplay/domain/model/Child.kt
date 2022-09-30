package com.movetoplay.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Child(
    @SerialName("fullName")
    var name: String = "",
    @SerialName("sex")
    var gender: Gender = Gender.MAN,
    @SerialName("age")
    var age: Int = 0,
    var isEngagedSports: Boolean = false,
    val id: String = "",
    val parentAccountId: String = ""
)