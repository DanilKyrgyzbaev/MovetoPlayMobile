package com.movetoplay.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.user_apps.AppBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.AccessibilityPrefs
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

        }
    }

    private fun compareLists(listDto: List<UserApp>?, context: Context) {
        val listLoc = getApps(context)
        if (listDto?.isEmpty() != true) {
            val postList = ArrayList<AppBody>()

            listDto?.forEach { dto ->
                val app = AppBody(dto.name, dto.packageName)
                if (listLoc.apps?.contains(app) != true) {
                    postList.add(app)
                }
            }
            if (postList.isNotEmpty()) {
                Log.e("main", "compareLists diff: $postList")
                postApps(UserAppsBody(postList), context)
            }
        } else postApps(listLoc, context)
    }

    private fun postApps(apps: UserAppsBody, context: Context) {
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
                            syncApps(context)
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
}