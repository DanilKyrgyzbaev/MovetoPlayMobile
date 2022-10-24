package com.movetoplay.screens.confirm_accounts // ktlint-disable package-name

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.movetoplay.model.AccountsConfirm
import com.movetoplay.model.ErrorResponse
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmAccountsViewModel : ViewModel() {
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()
    val mutableLiveData = MutableLiveData<Boolean>()

    fun confirmAccounts(accountsConfirm: AccountsConfirm) {
        val response = api.confirmAccounts("Bearer ${Pref.accessToken}", accountsConfirm)
        response.enqueue(object : Callback<AccountsConfirm> {
            override fun onResponse(call: Call<AccountsConfirm>, response: Response<AccountsConfirm>) {
                if (response.isSuccessful) {
                    if (mutableLiveData.value == null) {
                        mutableLiveData.value = true
                    }
                } else {
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    Pref.toast = error.toString()
                    Log.e("ResponseError", error.toString())
                    errorHandle.value = error
                }
            }

            override fun onFailure(call: Call<AccountsConfirm>, t: Throwable) {
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
