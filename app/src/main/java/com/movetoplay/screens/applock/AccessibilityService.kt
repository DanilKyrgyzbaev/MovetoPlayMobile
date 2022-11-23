package com.movetoplay.screens.applock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.google.gson.reflect.TypeToken
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.Pref
import com.movetoplay.screens.ChildLockActivity
import com.movetoplay.services.ResetAlarmManager
import com.movetoplay.util.parseArray
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AccessibilityService : AccessibilityService() {

    @Inject
    lateinit var apiRepository: UserAppsRepository
    private var timer: CountDownTimer? = null
    private val serviceJob = Job()
    private val usedTimeDay = 24 * 60 * 60L
    private var usedTime = 0L
    private var runningApp: UserApp = UserApp()
    private val serviceCoroutineScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private var userApps = ArrayList<UserApp>()
    private lateinit var resetAlarmManager: ResetAlarmManager

    override fun onServiceConnected() {
        Log.e("onServiceConnected", "onServiceConnected: " + AccessibilityPrefs.currentDay)

        resetAlarmManager = ResetAlarmManager()
        resetAlarmManager.setAlarm(this.applicationContext)
        super.onServiceConnected()
    }

    private fun updateUserApps() {
        val apps = ArrayList<UserApp>()
        AccessibilityPrefs.getLimitedAppsById(Pref.childId)?.let {
            parseArray<ArrayList<UserApp>>(it, object : TypeToken<ArrayList<UserApp>>() {}.type)
        }?.let { apps.addAll(it) }

        if (apps.isNotEmpty()) {
            userApps = apps
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
        if (event.packageName == null) {
            Log.e("access", "Event package is NULL")
            return
        }
        updateUserApps()

        val eventPackage = event.packageName.toString()
        Log.e("access", "eventPackageName  $eventPackage")
        if (!eventPackage.equals(
                packageName.toString(),
                ignoreCase = true
            )
        ) {
            if (eventPackage != runningApp.packageName) {
                timer?.cancel()
                if (runningApp.packageName != "") syncData(runningApp)
                Log.e("access", "runningApp: $runningApp")
                userApps.map { app ->
                    Log.e("access", "Event app package: ${app.packageName} type: ${app.type}")
                    if (app.packageName == eventPackage && app.type == "allowed") {
                        startTimer(
                            false,
                            remainTime = null,
                            usedTimeDay = usedTimeDay
                        )
                        runningApp = app
                        return
                    }
                    if (app.packageName == eventPackage && app.type == "unallowed") {
                        startTimer(
                            true,
                            AccessibilityPrefs.remainingTime
                        )
                        runningApp = app
                        return
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

    private fun syncData(app: UserApp) {
        val min: Long = TimeUnit.MILLISECONDS.toMinutes(usedTime)
        Log.e("access", "syncData appName = ${app.name} usedTime:$min, sec:$usedTime")
        if (min != 0L)
            serviceCoroutineScope.launch {
                try {
                    val result = apiRepository.setAppUsedTime(app.id, Limited(min, app.type))
                    Log.e("access", "syncData: $result")
                    runningApp = UserApp()
                    usedTime = 0L
                } catch (e: Throwable) {
                    Log.e("access", "syncData error: ${e.message}")
                }
            }
    }

    private fun openLockScreen() {
        Log.e("access", "openLockScreen: ")
        val lockIntent = Intent(
            this,
            ChildLockActivity::class.java
        )
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(lockIntent)
    }

    private fun startTimer(
        isCountDown: Boolean,
        remainTime: Long? = null,
        usedTimeDay: Long? = null
    ) {
        val timeDuration = remainTime ?: usedTimeDay
        Log.e(
            "access",
            "startTimer:$timeDuration, remainingTime:$remainTime, isCountDown:$isCountDown"
        )
        timer = object : CountDownTimer(timeDuration!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isCountDown)
                    AccessibilityPrefs.remainingTime = millisUntilFinished

                usedTime += 1000
            }

            override fun onFinish() {
                syncData(runningApp)
                openLockScreen()
            }
        }
        timer?.start()
    }

    private fun blockGooglePlay() {
        val intent = Intent(this, LockScreenChildActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onInterrupt() {
        serviceJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        resetAlarmManager.cancelAlarm(this.applicationContext)
    }
}