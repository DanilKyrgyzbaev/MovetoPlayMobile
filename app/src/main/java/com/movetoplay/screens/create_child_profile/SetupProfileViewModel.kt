package com.movetoplay.screens.create_child_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movetoplay.model.CreateProfile
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetupProfileViewModel: ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()

    fun sendProfileChild(token: String, createProfile: CreateProfile, activity: SetupProfileActivity){
        val response = api.postChildProfile(token,createProfile)
        response.enqueue(object : Callback<CreateProfile>{
            override fun onResponse(call: Call<CreateProfile>, response: Response<CreateProfile>) {
                if (response.isSuccessful){
                    response.body()
                    Log.e("ResponseProfile",response.body().toString())
                } else{
                    response.errorBody()
                    Log.e("Response","${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<CreateProfile>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure","${t.message}")
            }

        })
    }

    //    fun updateUser(token: String,user: User){
    //        val response = api.updateUser(token,user)
    //        response.enqueue(object : Callback<User>{
    //            override fun onResponse(call: Call<User>, response: Response<User>) {
    //                if (response.isSuccessful){
    //                    response.body()
    //                    Log.e("UpdateUser","${response.body()}")
    //                } else {
    //                    response.errorBody()
    //                }
    //            }
    //            override fun onFailure(call: Call<User>, t: Throwable) {
    //                errorHandle.postValue(t.message)
    //                Log.e("UpdateUser","${t.message}")
    //            }
    //
    //        })
    //    }
}