package com.movetoplay.screens.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movetoplay.model.Registration
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
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
                    Log.e("Token","${response.body()?.token}")
                    Log.e("ResponseSuccess", response.body().toString())
                    Pref.userToken = response.body()?.token.toString()
                } else {
                    response.errorBody()
                    Log.e("ResponseError","${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Registration>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure","${t.message}")
            }
        })
    }
}