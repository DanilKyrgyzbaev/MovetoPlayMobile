package com.movetoplay.screens.applock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.user_apps.PinBody
import com.movetoplay.data.repository.ProfilesRepositoryImpl
import com.movetoplay.depen_inject.RemoteClientModule.provideApi
import com.movetoplay.depen_inject.RemoteClientModule.provideOkHttpClient
import com.movetoplay.depen_inject.RemoteClientModule.provideRetrofit
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LockScreenViewModel() : ViewModel() {

    val pinResult = MutableLiveData<HashMap<String, String>>()

    private val profilesRepositoryImpl = ProfilesRepositoryImpl(
        provideApi(
            provideRetrofit(
                provideOkHttpClient()
            )
        )
    )

    fun setPinCode(pinBody: PinBody) {
        pinResult.value = hashMapOf("load" to "")
        viewModelScope.launch(Dispatchers.IO) {
            if (Pref.childToken != "") {
                try {
                    pinResult.postValue(hashMapOf(
                        "success" to "${profilesRepositoryImpl.setPinCode(pinBody)}"
                    ))
                } catch (e: Throwable) {
                    pinResult.postValue(hashMapOf("error" to e.message.toString()))
                }
            } else pinResult.postValue(hashMapOf("error" to "Ошибка авторизации"))
        }
    }
}
