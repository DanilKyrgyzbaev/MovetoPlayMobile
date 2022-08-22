package com.movetoplay.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val statusCode: Int? = null,
    val message: String? = null
)