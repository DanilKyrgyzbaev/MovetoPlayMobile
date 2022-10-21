package com.movetoplay.domain.utils

sealed class ResultStatus<out T> {
    class Loading<T> : ResultStatus<T>()
    class Success<T>(val data: T?) : ResultStatus<T>()
    class Error<T>(val error: String?) : ResultStatus<T>()
}
