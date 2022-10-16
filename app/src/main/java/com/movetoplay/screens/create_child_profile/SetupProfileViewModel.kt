package com.movetoplay.screens.create_child_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.movetoplay.model.CreateProfile
import com.movetoplay.model.ErrorResponse
import com.movetoplay.model.Registration
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
import com.movetoplay.screens.register.RegisterActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetupProfileViewModel: ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()
    val mutableLiveData = MutableLiveData<Boolean>()

    fun sendProfileChild(token: String, createProfile: CreateProfile){
        val response = api.postChildProfile(token,createProfile)
        response.enqueue(object : Callback<CreateProfile>{
            override fun onResponse(call: Call<CreateProfile>, response: Response<CreateProfile>) {
                if (response.isSuccessful){
                    response.body()?.id
                    Pref.childId = response.body()?.id.toString()
                    mutableLiveData.value = true
                    Log.e("ResponseProfile",response.body().toString())
                } else{
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    response.errorBody()
                    Log.e("Response","${response.errorBody()}")
                    errorHandle.value = error
                }
            }

            override fun onFailure(call: Call<CreateProfile>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure","${t.message}")
                errorHandle.value = t.message
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