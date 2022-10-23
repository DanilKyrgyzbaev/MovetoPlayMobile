package com.movetoplay.screens.applock

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.google.gson.Gson
import com.movetoplay.data.repository.UserAppsRepositoryImpl
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.Pref
import com.movetoplay.screens.ChildLockActivity
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

@AndroidEntryPoint
class AccessibilityServiceVersion2 : AccessibilityService() {

    @Inject
    lateinit var apiRepository: UserAppsRepository
    private var timeDuration: Long = 0
    private var timer: CountDownTimer? = null
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var userApps = HashMap<String, UserApp>()

    override fun onServiceConnected() {
        Log.e("onServiceConnected", "onServiceConnected: " + AccessibilityPrefs.currentDay)
        AccessibilityPrefs.currentDay = System.currentTimeMillis()

        getUserApps()

        super.onServiceConnected()
    }

    private fun getUserApps() {
        serviceScope.launch {
            apiRepository.getLimitedApps(Pref.accessToken, Pref.childId).collect {
                if (it is ResultStatus.Success) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.forEach {app->
                            userApps[app.packageName] = app
                        }
                    }
                }
                if (it is ResultStatus.Error) {
                    Log.e("access", "getUserApps: ${it.error}")
                }
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.e(
            "access",
            "----------------------------------Start----------------------------------------"
        )
        Log.e("access", "onAccessibilityEvent:  ${event.packageName}")
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        checkDay()

        val eventPackage = event.packageName.toString()
        Log.e("access", "Event package: $eventPackage")
        if (!eventPackage.equals(packageName.toString(), ignoreCase = true)) {
            if (AccessibilityPrefs.lastPackage == "") {
                userApps.forEach { app ->
                    Log.e("access", "Event app package: ${app.key} type: ${app.value.type}")
                    if (app.key == eventPackage && app.value.type == "limited") return
                    if (app.key == eventPackage && app.value.type != "limited") {
                        Log.e("access", "Is timer running: ${AccessibilityPrefs.isTimerRunning}")
                        if (!AccessibilityPrefs.isTimerRunning) {
                            AccessibilityPrefs.isTimerRunning = true
                            AccessibilityPrefs.lastPackage = eventPackage
                            startTimer()
                        }
                    }
                }
            } else {
                Log.e("access", "Event lastPackage: ${AccessibilityPrefs.lastPackage}")
                if (AccessibilityPrefs.lastPackage != eventPackage) {
                    Log.e("access", "Is timer running: ${AccessibilityPrefs.isTimerRunning}")
                    if (AccessibilityPrefs.isTimerRunning) {
                        AccessibilityPrefs.lastPackage = ""
                        AccessibilityPrefs.isTimerRunning = false
                        timer?.cancel()
                        Log.e("accessTime", "Remaining time: " + getRemainingTimePrefs().toString())
                    }
                }
            }
        }
        if ("com.android.vending" == packageName) {
            blockGooglePlay()
        }

        Log.e(
            "access",
            "---------------------------------------End----------------------------------- ",
        )
    }

    private fun checkDay() {
        val diff = AccessibilityPrefs.currentDay - getTimeMillis()
        if (TimeUnit.MILLISECONDS.toDays(diff) < 0) {
            Log.e("TAG", "checkDay: DNI OTLICHAYUTSYA : " + TimeUnit.MILLISECONDS.toDays(diff))
            AccessibilityPrefs.currentDay = getTimeMillis()
            AccessibilityPrefs.remainingTime = AccessibilityPrefs.dailyLimit
        } else Log.e(
            "TAG",
            "DAY IS SAME : " + TimeUnit.MILLISECONDS.toDays(diff) + " second: " + TimeUnit.MILLISECONDS.toSeconds(
                diff
            )
        )
    }

    private fun openLockScreen() {
        val intent = Intent(this, ChildLockActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun startTimer() {
        val timeDuration = getRemainingTimePrefs()
        timer = object : CountDownTimer(timeDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                AccessibilityPrefs.remainingTime = millisUntilFinished
            }

            override fun onFinish() {
                AccessibilityPrefs.isTimerRunning = false
                AccessibilityPrefs.lastPackage = ""
                openLockScreen()
            }
        }
        timer?.start()
    }

    private fun getRemainingTimePrefs(): Long {
        timeDuration = AccessibilityPrefs.remainingTime
        return timeDuration
    }

    private fun blockGooglePlay() {
        val intent = Intent(this, LockScreenChildActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onInterrupt() {}

    interface Callback {
        fun onFailed()
        fun onSuccess()
    }

    companion object {
        var callback: Callback? = null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}