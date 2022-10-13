package com.movetoplay.screens.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.movetoplay.model.ErrorResponse
import com.movetoplay.model.Registration
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()

    fun sendUser(register: Registration, activity: RegisterActivity){
        val response = api.postUserRegister(register)
        response.enqueue(object : Callback<Registration>{
            override fun onResponse(call: Call<Registration>, response: Response<Registration>) {
                if (response.isSuccessful){
                    response.body()?.token
                    Pref.userToken = response.body()?.token.toString()
                } else {
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    Pref.toast = error.toString()
                    Log.e("ResponseError",error.toString())
                }
            }

            override fun onFailure(call: Call<Registration>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure","${t.message}")
            }
        })
    }

    protected inline fun <reified ErrorType> ResponseBody?.toApiError(): ErrorType {
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