package com.movetoplay.screens.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.movetoplay.model.ErrorResponse
import com.movetoplay.domain.model.User
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInViewModel : ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()
    val mutableLiveData = MutableLiveData<Boolean>()
    val isChild = MutableLiveData<Boolean>()

    fun sendUser(user: User) {
        if (isChild.value == true) sendUserApps()
        api.login(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Pref.accessToken = response.body()?.token.toString()
                    Log.e("Token", response.body()?.token.toString())
                    viewModelScope.launch {
                        val getParentInfo = api.geInfo("Bearer ${Pref.accessToken}")
                        if (getParentInfo.isSuccessful) {
                            Pref.parentId = getParentInfo.body()?.id.toString()
                            mutableLiveData.value = true
                            Log.e("TAG", "onResponse: arent get SUCCESS")
                        } else {
                            Log.e("TAG", "onResponse: parent get info failed")
                            mutableLiveData.value = true
                        }
                    }
                } else {
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    Log.e("Error", error.toString())
                    errorHandle.value = error
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure", "${t.message}")
                errorHandle.value = t.message
            }
        })
    }

    private fun sendUserApps() {
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