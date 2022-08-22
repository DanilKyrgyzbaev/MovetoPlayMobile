package com.movetoplay.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationLock(
    val name: String,
    val namePackage: String,
    val type: TypeLock
)

@Serializable
sealed class TypeLock{
    @Serializable
    @SerialName("FreeUse")
    object FreeUse: TypeLock()
    @Serializable
    @SerialName("TimeUse")
    class TimeUse(val minutes: Int): TypeLock()
    @Serializable
    @SerialName("ProhibitedUse")
    object ProhibitedUse: TypeLock()
}