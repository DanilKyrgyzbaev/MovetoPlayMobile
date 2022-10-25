package com.movetoplay.screens.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    val resultStatus = MutableLiveData<ResultStatus<Boolean>>()

    fun register(email: String, pass: String, age: String) {
        viewModelScope.launch {
            resultStatus.value = ResultStatus.Loading()
            when (val result = authRepository.signUp(email, pass, age.toInt())) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Success -> {
                    Pref.userAccessToken = result.data?.accessToken.toString()
                    Pref.userRefreshToken = result.data?.refreshToken.toString()
                    Log.e("reg", "register token: ${Pref.userAccessToken}", )
                    resultStatus.value = ResultStatus.Success(true)
                }
                is ResultStatus.Error -> {
                    resultStatus.value = ResultStatus.Error(result.error)
                }
            }
        }
    }
}