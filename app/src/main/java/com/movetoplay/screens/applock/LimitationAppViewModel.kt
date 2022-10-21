package com.movetoplay.screens.applock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.HashSet
import javax.inject.Inject

@HiltViewModel
class LimitationAppViewModel @Inject constructor(
    val repository: UserAppsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var authorizeResult = MutableLiveData<Boolean>()
    var userApps = MutableLiveData<ResultStatus<List<UserApp>>>()
    val sendResultStatus = MutableLiveData<ResultStatus<Boolean>>()
    val loading = MutableLiveData<ResultStatus<Boolean>>()

    fun getLimited(id: String) {
        viewModelScope.launch {
            repository.getLimitedApps(Pref.userToken, id).collect { appsResponse ->
                when (appsResponse) {
                    is ResultStatus.Loading -> {
                        userApps.value = ResultStatus.Loading()
                    }
                    is ResultStatus.Success -> {
                        userApps.value = ResultStatus.Success(appsResponse.data)
                        if (Pref.childToken == "") {
                            val app = appsResponse.data as List<UserApp>
                            if (app.isNotEmpty()) {
                                app[0].let { uApp ->
                                    val res = authRepository.authorizeProfile(
                                        uApp.profileId,
                                        uApp.deviceId
                                    )
                                    if (res is ResultStatus.Success)
                                        Pref.childToken = res.data?.token.toString()

                                }
                            }
                        }
                    }
                    is ResultStatus.Error -> {
                        userApps.value = ResultStatus.Error(appsResponse.error)
                    }
                }
            }
        }
    }

    fun setLimit(app: UserApp) {
        viewModelScope.launch {
            if (Pref.childToken == "") {
                when (val res = authRepository.authorizeProfile(app.profileId, app.deviceId)) {
                    is ResultStatus.Error -> {
                        authorizeResult.value = false
                    }
                    is ResultStatus.Success -> {
                        Pref.childToken = res.data?.token.toString()
                        repository.setLimitedApp(
                            app.id,
                            Limited(AccessibilityPrefs.dailyLimit, app.type)
                        ).collect {
//                            when (it) {
//                                is ResultStatus.Success -> {
//                                    loading.value = ResultStatus.Success(true)
//                                }
//                                is ResultStatus.Error -> {
//                                    loading.value = ResultStatus.Error(it.error)
//                                }
//                                is ResultStatus.Loading -> {
//                                    loading.value = ResultStatus.Loading()
//                                }
//                            }
                        }
                    }
                    else -> {}
                }
            } else {
                repository.setLimitedApp(
                    app.id,
                    Limited(AccessibilityPrefs.dailyLimit, app.type)
                ).collect {
//                    when (it) {
//                        is ResultStatus.Success -> {
//                            loading.value = ResultStatus.Success(true)
//                        }
//                        is ResultStatus.Error -> {
//                            loading.value = ResultStatus.Error(it.error)
//                        }
//                        is ResultStatus.Loading -> {
//                            loading.value = ResultStatus.Loading()
//                        }
//                    }
                }
            }
        }
    }

    fun setLimits(blockedApps: HashSet<String>) {
        viewModelScope.launch {
            loading.value = ResultStatus.Loading()
            if (Pref.childToken != "") {
                blockedApps.forEach { id ->
                    repository.setLimitedApp(
                        id,
                        Limited(AccessibilityPrefs.dailyLimit, "unlimited")
                    ).collect {
                        if (it is ResultStatus.Error)
                            loading.value = ResultStatus.Error(it.error)
                    }
                }
                loading.value = ResultStatus.Success(true)
            } else loading.value = ResultStatus.Error("Ошика авторизации!")
        }
    }
}