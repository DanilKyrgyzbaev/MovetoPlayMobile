package com.movetoplay.screens.forgot_password // ktlint-disable package-name

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.movetoplay.model.ChangePasswordByCode
import com.movetoplay.model.ErrorResponse
import com.movetoplay.model.JwtSessionToken
import com.movetoplay.model.RememberPassword
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel : ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()
    val mutableLiveDataAccountId = MutableLiveData<Boolean>()
    val mutableLiveDataJwtSessionToken = MutableLiveData<Boolean>()
    val mutableLiveDataPasswordByCode = MutableLiveData<Boolean>()

    fun getAccountId(email: String) {
        api.rememberPassword(email).enqueue(object : Callback<RememberPassword> {
            override fun onResponse(
                call: Call<RememberPassword>,
                response: Response<RememberPassword>
            ) {
                if (response.isSuccessful) {
                    response.body()
                    Pref.accountId = response.body()?.accountId.toString()
                    Log.e("AccountId", Pref.accountId)
                    mutableLiveDataAccountId.value = true
                } else {
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    response.errorBody()
                    Pref.toast = error.toString()
                    Log.e("ResponseError", error.toString())
                    errorHandle.value = error
                }
            }

            override fun onFailure(call: Call<RememberPassword>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure", "${t.message}")
                errorHandle.value = t.message
            }
        })
    }

    fun getJwtSessionToken(jwtSessionToken: JwtSessionToken) {
        api.getJwtSessionToken(jwtSessionToken).enqueue(object : Callback<JwtSessionToken> {
            override fun onResponse(
                call: Call<JwtSessionToken>,
                response: Response<JwtSessionToken>
            ) {
                if (response.isSuccessful) {
                    Pref.jwtSessionToken = response.body()?.jwtSessionToken.toString()
                    Log.e("AccountId", Pref.accountId)
                    mutableLiveDataJwtSessionToken.value = true
                    Log.e("response", "${response.body()}")
                } else {
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    response.errorBody()
                    Pref.toast = error.toString()
                    Log.e("ResponseError", error.toString())
                    errorHandle.value = error
                }
            }

            override fun onFailure(call: Call<JwtSessionToken>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure", "${t.message}")
                errorHandle.value = t.message
            }
        })
    }

    fun changePasswordByCode(changePasswordByCode: ChangePasswordByCode) {
        api.changePasswordByCode("Bearer ${Pref.jwtSessionToken}", changePasswordByCode).enqueue(object : Callback<ChangePasswordByCode> {
            override fun onResponse(
                call: Call<ChangePasswordByCode>,
                response: Response<ChangePasswordByCode>
            ) {
                if (response.isSuccessful) {
                    response.body()
                    mutableLiveDataPasswordByCode.value = true
                } else {
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    response.errorBody()
                    Pref.toast = error.toString()
                    Log.e("ResponseError", error.toString())
                    errorHandle.value = error
                }
            }

            override fun onFailure(call: Call<ChangePasswordByCode>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure", "${t.message}")
                errorHandle.value = t.message
            }
        })
    }

    private inline fun <reified ErrorType> ResponseBody?.toApiError(): ErrorType {
        val errorJson = this?.string()
        Log.e("retrying", "to api error body $errorJson")
        val data = Gson().fromJson(
            errorJson,
            ErrorType::class.java
        )
        Log.e("retrying", "to api error ${Gson().toJson(data)})")
        return data
    }
}
