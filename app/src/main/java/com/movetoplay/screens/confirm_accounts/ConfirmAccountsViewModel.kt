package com.movetoplay.screens.confirm_accounts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ConfirmAccountsViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    val resultStatus = MutableLiveData<ResultStatus<Boolean>>()
    val resultStatusResendConfirmCode = MutableLiveData<ResultStatus<Boolean>>()

    fun confirmEmail(code: Int) {
        Log.e("reg", "confirmEmail: $code")
        viewModelScope.launch {
            resultStatus.value = ResultStatus.Loading()
            when (val result = authRepository.confirm(code)) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Success -> {
                    Log.e("reg", "confirmEmail: success")
                    resultStatus.value = ResultStatus.Success(true)
                }
                is ResultStatus.Error -> {
                    Log.e("reg", "confirmEmail: error ${result.error}")
                    resultStatus.value = ResultStatus.Error(result.error)
                }
            }
        }
    }

    fun resendConfirmCode() {
        viewModelScope.launch {
            resultStatusResendConfirmCode.value = ResultStatus.Loading()
            when (val result = authRepository.resendConfirmCode()) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Success -> {
                    Log.e("reg", "confirmEmail: success")
                    resultStatusResendConfirmCode.value = ResultStatus.Success(true)
                }
                is ResultStatus.Error -> {
                    Log.e("reg", "confirmEmail: error ${result.error}")
                    resultStatusResendConfirmCode.value = ResultStatus.Error(result.error)
                }
            }
        }
    }
}
