package com.movetoplay.domain.utils

sealed class RequestStatus{
    class Error<T>(val data: T? = null, val message: String? = null) : RequestStatus()
    class Loading<T>(val data: T? = null, val message: String? = null) : RequestStatus()
    class Success<T>(val data: T? = null, val message: String? = null) : RequestStatus()
    object Null : RequestStatus()
}
