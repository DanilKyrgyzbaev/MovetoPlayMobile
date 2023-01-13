package com.movetoplay.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Child(
    var fullName: String = "",
    var sex: Gender = Gender.man,
    var age: Int = 0,
    var needSeconds: String = "",
    var needJumpCount: Int = 0,
    var needSquatsCount: Int = 0,
    var needSqueezingCount: Int = 0,
    var pinCode: String = "",
    var isEnjoySport: Boolean = false,
    val id: String = "",
    val parentAccountId: String = ""
)