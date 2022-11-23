package com.movetoplay.domain.model

data class ChildInfo(
    val age: Int = 0,
    val createdAt: String = "",
    val fullName: String = "",
    val id: String = "",
    var pinCode: String? = null,
    val isEnjoySport: Boolean = false,
    val needJumpCount: Int? = null,
    val needSeconds: String? = null,
    val needSquatsCount: Int? = null,
    val needSqueezingCount: Int? = null,
    val parentAccount: ParentAccount? = null,
    val parentAccountId: String = "",
    val sex: String = "",
    val updatedAt: String = ""
)