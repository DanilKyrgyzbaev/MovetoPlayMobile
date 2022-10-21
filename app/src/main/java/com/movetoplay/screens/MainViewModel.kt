package com.movetoplay.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.user_apps.AppBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.screens.applock.ApkInfoExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userAppsRepository: UserAppsRepository,
) : ViewModel() {

    fun syncApps(context: Context) {
        if (Pref.childToken!="" && Pref.isFirst) {
            viewModelScope.launch {
                userAppsRepository.postLimitedApps(Pref.childToken, getApps(context))
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
    }

    private fun getApps(context: Context): UserAppsBody {
        val apps = ArrayList<AppBody>()
        val extractor = ApkInfoExtractor(context)

        extractor.GetAllInstalledApkInfo().forEach {
            apps.add(AppBody(extractor.GetAppName(it), it))
        }
        return UserAppsBody(apps)
    }
}