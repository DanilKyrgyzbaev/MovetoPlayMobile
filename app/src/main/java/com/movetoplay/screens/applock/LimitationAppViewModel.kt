package com.movetoplay.screens.applock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LimitationAppViewModel @Inject constructor(
    val repository: UserAppsRepository,
    private val authRepository: AuthRepository,
    private val profilesRepository: ProfilesRepository
) : ViewModel() {

    var userApps = MutableLiveData<ResultStatus<List<UserApp>>>()
    val loading = MutableLiveData<ResultStatus<Boolean>>()

    fun getUserApps(id: String) {
        viewModelScope.launch {
            repository.getLimitedApps(Pref.userAccessToken, id).collect { appsResponse ->
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
                                        res.data?.accessToken?.let { token ->
                                            Pref.childToken = token
                                        }
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

    fun setLimits(blockedApps: HashMap<String, String>) {
        viewModelScope.launch {
            var success = 0
            loading.value = ResultStatus.Loading()
            if (Pref.childToken != "") {
                blockedApps.forEach { id ->
                    repository.setLimitedApp(
                        id.key,
                        Limited(AccessibilityPrefs.dailyLimit, id.value)
                    ).collect {
                        if (it is ResultStatus.Error)
                            loading.value = ResultStatus.Error(it.error)
                        if (it is ResultStatus.Success){
                            ++success
                        }
                    }
                }
                if (success==blockedApps.size){
                    loading.value = ResultStatus.Success(true)
                }
            } else loading.value = ResultStatus.Error("Ошибка авторизации!")
        }
    }
}