package com.movetoplay.screens.applock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.user_apps.PinBody
import com.movetoplay.data.repository.ProfilesRepositoryImpl
import com.movetoplay.depen_inject.RemoteClientModule.provideApi
import com.movetoplay.depen_inject.RemoteClientModule.provideOkHttpClient
import com.movetoplay.depen_inject.RemoteClientModule.provideRetrofit
import com.movetoplay.pref.Pref
import kotlinx.coroutines.launch

class LockScreenViewModel() : ViewModel() {

    val pinResult = MutableLiveData<Boolean>()

    private val profilesRepositoryImpl = ProfilesRepositoryImpl(
        provideApi(
            provideRetrofit(
                provideOkHttpClient()
            )
        )
    )

    fun setPinCode(pinBody: PinBody) {
        viewModelScope.launch {
            if (Pref.childToken != "") {
                profilesRepositoryImpl.setPinCode(Pref.childId, pinBody).collect{
                    pinResult.value = it
                }
            } else pinResult.value = false
        }
    }
}
