package com.movetoplay.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.movetoplay.data.repository.AuthRepositoryImpl
import com.movetoplay.depen_inject.RemoteClientModule
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
    private var authRepository: AuthRepositoryImpl = AuthRepositoryImpl(
        RemoteClientModule.provideApi(
            RemoteClientModule.provideRetrofit(RemoteClientModule.provideOkHttpClient())
        )
    )

    val signViaGoogleResult = MutableLiveData<HashMap<String, String>>()

    fun signViaGoogle(token: String) {

        signViaGoogleResult.value = hashMapOf("loading" to "yes")

        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful)
                signOrRegister(it.result.additionalUserInfo?.isNewUser ?: false)
            else signViaGoogleResult.value =
                hashMapOf("error" to "Error: " + it.exception?.localizedMessage)
        }
    }

    fun signOrRegister(isNewUser: Boolean) {
        viewModelScope.launch {
            val result = auth.currentUser?.getIdToken(true)?.await()
            if (result?.token != null) {
                Pref.fidToken = result.token
                if (isNewUser)
                    login(result.token!!)
                else register(result.token!!)
            } else signViaGoogleResult.value = hashMapOf("error" to "Invalid token")
        }
    }

     fun register(token: String) {
        viewModelScope.launch {
            when (val result = authRepository.registerViaGoogle(token)) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Success -> {
                    Pref.userAccessToken = result.data?.accessToken.toString()
                    signViaGoogleResult.value = hashMapOf("success" to "yes")
                }
                is ResultStatus.Error -> {
                    signViaGoogleResult.value = hashMapOf("error" to "Error: " + result.error)
                }
            }
        }
    }

    private fun login(token: String) {
        viewModelScope.launch {
            when (val result = authRepository.loginViaGoogle(token)) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Success -> {
                    Pref.userAccessToken = result.data?.accessToken.toString()
                    signViaGoogleResult.value = hashMapOf("success" to "yes")
                }
                is ResultStatus.Error -> {
                    signViaGoogleResult.value = hashMapOf("error" to "Error: " + result.error)
                }
            }
        }
    }
}