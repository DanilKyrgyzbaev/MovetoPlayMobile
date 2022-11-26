package com.movetoplay.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.user_apps.AppBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.DeviceRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.Pref
import com.movetoplay.screens.applock.ApkInfoExtractor
import com.movetoplay.util.getMacAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userAppsRepository: UserAppsRepository,
    private val deviceRepository: DeviceRepository,
    private val profilesRepository: ProfilesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun syncApps(context: Context) {
        if (Pref.childToken != "") {
            viewModelScope.launch {
                userAppsRepository.getLimitedApps(Pref.userAccessToken, Pref.childId).collect {
                    when (it) {
                        is ResultStatus.Loading -> {}
                        is ResultStatus.Success -> {
                            it.data?.let { apps ->
                                AccessibilityPrefs.setLimitedAppsById(
                                    Pref.childId,
                                    apps as ArrayList<UserApp>
                                )
                            }
                            compareLists(it.data, context)
                        }
                        is ResultStatus.Error -> {
                            Log.e("main", "syncApps: error: ${it.error}")
                        }
                    }
                }
            }
        } else authorizeProfile(context)
    }

    private fun authorizeProfile(context: Context) {
        viewModelScope.launch {
            val result = deviceRepository.getDeviceByMacAddress(Pref.childId, getMacAddress())
            if (result is ResultStatus.Success) {
                result.data?.id?.let {
                    when (val auth = authRepository.authorizeProfile(Pref.childId, it)) {
                        is ResultStatus.Success -> {
                            auth.data?.let { response ->
                                Pref.childToken = response.accessToken
                            }
                            syncApps(context)
                        }
                        is ResultStatus.Error -> {
                            Log.e("main", "authorizeProfile error: " + auth.error)
                        }
                        else -> {}
                    }
                }
            } else if (result is ResultStatus.Error) Log.e(
                "main",
                "authorizeProfile error: " + result.error
            )
        }
    }

    private  fun compareLists(listDto: List<UserApp>?, context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            val listLoc = getApps(context)
            val appsDto = ArrayList<AppBody>()
            listDto?.map {
                appsDto.add(AppBody(it.name,it.packageName))
            }
            if (appsDto.isNotEmpty()) {
                val postList = ArrayList<AppBody>()
                if (appsDto.size != listLoc.apps?.size) {
                      listLoc.apps?.map{
                          if (!appsDto.contains(it)){
                              postList.add(it)
                          }
                      }
                }

                if (postList.isNotEmpty()) {
                    Log.e("main", "compareLists diff: $postList")
                    postApps(UserAppsBody(postList))
                }
            } else postApps(listLoc)
        }
    }

    private fun postApps(apps: UserAppsBody) {
        viewModelScope.launch {
            userAppsRepository.postLimitedApps(Pref.childToken, apps)
                .collect { result ->
                    when (result) {
                        is ResultStatus.Loading -> {}
                        is ResultStatus.Success -> {
                            Log.e(
                                "authorize",
                                "syncApps: SUCCESS" + result.data
                            )
                        }
                        is ResultStatus.Error -> {
                            Log.e(
                                "authorize",
                                "syncApps ERROR: " + result.error
                            )
                        }
                    }
                }
        }
    }

    private fun getApps(context: Context): UserAppsBody {
        val apps = ArrayList<AppBody>()
        val extractor = ApkInfoExtractor(context)

        extractor.GetAllInstalledApkInfo().forEach {
            apps.add(AppBody(extractor.GetAppName(it), it))
        }
        return UserAppsBody(apps)
    }

    fun getChildInfo(context:Context) {
        if (Pref.childToken != "") {
            viewModelScope.launch {
                try {
                    AccessibilityPrefs.childInfo = profilesRepository.getInfo(Pref.childId)
                    Log.e("main", "getChildInfo: pref" + AccessibilityPrefs.childInfo)
                } catch (e: Throwable) {
                    Log.e("main", "getChildInfo: ${e.message}",)
                }
            }
        } else {
            Toast.makeText(context, "Ошибка авторизации!", Toast.LENGTH_SHORT).show()
        }
    }
}