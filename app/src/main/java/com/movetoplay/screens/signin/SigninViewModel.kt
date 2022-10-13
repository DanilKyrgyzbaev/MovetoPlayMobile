package com.movetoplay.screens.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.movetoplay.model.ErrorResponse
import com.movetoplay.model.ResponseSucces
import com.movetoplay.model.User
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninViewModel: ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()
    val mutableLiveData = MutableLiveData<User>()


    fun sendUser (user: User){
        api.login(user).enqueue(object : Callback<ResponseSucces>{
            override fun onResponse(call: Call<ResponseSucces>, response: Response<ResponseSucces>) {
                val loginResponse = response.body()
                if (loginResponse?.statusCode == 201){
                    Log.e("Token", loginResponse.token)
                }else{
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    Log.e("Error", error.toString())
                }
            }
            override fun onFailure(call: Call<ResponseSucces>, t: Throwable) {
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