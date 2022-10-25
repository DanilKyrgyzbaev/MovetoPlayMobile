package com.movetoplay.screens.signin

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.util.ValidationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    val resultStatus = MutableLiveData<ResultStatus<Boolean>>()

    fun login(context: Context, email: String, pass: String) {
        viewModelScope.launch {
            resultStatus.value = ResultStatus.Loading()
            if (ValidationUtil.isValidEmail(context, email) && ValidationUtil.isValidPassword(
                    context,
                    pass
                )
            ) {
                when (val result = authRepository.signIn(email, pass)) {
                    is ResultStatus.Loading -> {}
                    is ResultStatus.Success -> {
                        Pref.userAccessToken = result.data?.accessToken.toString()
                        Pref.userRefreshToken = result.data?.refreshToken.toString()
                        resultStatus.value = ResultStatus.Success(true)
                    }
                    is ResultStatus.Error -> {
                        resultStatus.value = ResultStatus.Error(result.error)
                    }
                }
            }
        }
    }
}