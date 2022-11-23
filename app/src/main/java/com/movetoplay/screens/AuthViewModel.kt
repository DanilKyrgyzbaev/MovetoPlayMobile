package com.movetoplay.screens

import android.util.Log
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
        Log.e("auth", token)
        signViaGoogleResult.value = hashMapOf("loading" to "yes")

        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                auth()
            } else signViaGoogleResult.value =
                hashMapOf("error" to "Error: " + it.exception?.localizedMessage)
        }
    }

    private fun auth() {
        viewModelScope.launch {
            val result = auth.currentUser?.getIdToken(true)?.await()
            if (result?.token != null) {
                when (val res = authRepository.signViaGoogle(result.token!!)) {
                    is ResultStatus.Loading -> {}
                    is ResultStatus.Success -> {
                        Pref.userAccessToken = res.data?.accessToken.toString()
                        Pref.userRefreshToken = res.data?.refreshToken.toString()
                        signViaGoogleResult.value = hashMapOf("success" to "yes")
                    }
                    is ResultStatus.Error -> {
                        signViaGoogleResult.value = hashMapOf("error" to "Error: " + res.error)
                    }
                }
            } else signViaGoogleResult.value = hashMapOf("error" to "Invalid token")
        }
    }
}
