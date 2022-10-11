package com.movetoplay.screens.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movetoplay.model.Registration
import com.movetoplay.model.User
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
import com.movetoplay.screens.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninViewModel: ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()

    fun sendUser(user: User, activity: SignInActivity){
        val response = api.postUser(user)
        response.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    response.body()?.token
                    Log.e("Token","${response.body()?.token}")
                    Pref.userToken = response.body()?.token.toString()
                } else {
                    response.errorBody()
                    Log.e("Response","${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure","${t.message}")

            }

        })
    }
}