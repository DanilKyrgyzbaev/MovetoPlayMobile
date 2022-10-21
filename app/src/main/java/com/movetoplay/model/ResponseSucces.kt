package com.movetoplay.model

import com.google.gson.annotations.SerializedName
import com.movetoplay.domain.model.User

data class ResponseSucces(
    @SerializedName("statusCode")
    var statusCode: Int,

    @SerializedName("message")
    var message: String,

    @SerializedName("token")
    var token: String,

    @SerializedName("user")
    var user: User
)
