package com.movetoplay.data.mapper

import android.util.Log
import com.google.gson.Gson
import okhttp3.ResponseBody

inline fun <reified ErrorType> ResponseBody?.toApiError(): ErrorType {
    val errorJson = this?.string()
    Log.e("retrying", "to api error body $errorJson")
    val data = Gson().fromJson(
        errorJson,
        ErrorType::class.java
    )
    Log.e("retrying", "to api error ${Gson().toJson(data)})")
    return data
}