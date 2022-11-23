package com.movetoplay.screens.applock

import androidx.lifecycle.LiveData
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
import com.yandex.metrica.impl.ob.id
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
    private val _setLimitAppCount = MutableLiveData(0)
    val setLimitAppCount: LiveData<Int> = _setLimitAppCount
    val childInfoResult = MutableLiveData<ResultStatus<ChildInfo>>()


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
            loading.value = ResultStatus.Loading()
            if (Pref.childToken != "") {
                blockedApps.forEach { id ->
                    repository.setLimitedApp(
                        id.key,
                        Limited(AccessibilityPrefs.dailyLimit, id.value)
                    ).collect {
                        if (it is ResultStatus.Error)
                            loading.value = ResultStatus.Error(it.error)
                    }
                }
                loading.value = ResultStatus.Success(true)
            } else loading.value = ResultStatus.Error("Ошибка авторизации!")
        }
    }
    fun setLimit(app: UserApp) {
        viewModelScope.launch {
            loading.value = ResultStatus.Loading()
            if (Pref.childToken != "") {
                    repository.setLimitedApp(
                        app.id,
                        Limited(AccessibilityPrefs.dailyLimit, app.type)
                    ).collect {
                        if (it is ResultStatus.Error)
                            loading.value = ResultStatus.Error(it.error)
                    }
                loading.value = ResultStatus.Success(true)
                _setLimitAppCount.value = _setLimitAppCount.value?.plus(1)
            } else loading.value = ResultStatus.Error("Ошибка авторизации!")
        }
    }
    fun getChildInfo() {
        childInfoResult.value = ResultStatus.Loading()
        viewModelScope.launch {
            try {
                childInfoResult.value =
                    ResultStatus.Success(profilesRepository.getInfo(Pref.childId))
            } catch (e: Throwable) {
                childInfoResult.value = ResultStatus.Error(e.message)
            }
        }
    }
}