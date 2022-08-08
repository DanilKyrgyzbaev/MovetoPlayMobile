package com.movetoplay.domain.model

sealed class RequestStatus{
    class Error(val data: Any? = null, val message: String? = null) : RequestStatus()
    class Loading(val data: Any? = null, val message: String? = null) : RequestStatus()
    class Success(val data: Any? = null, val message: String? = null) : RequestStatus()
    object Null : RequestStatus()
}
