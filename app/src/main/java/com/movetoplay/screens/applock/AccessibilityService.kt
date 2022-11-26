package com.movetoplay.screens.applock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.google.gson.reflect.TypeToken
import com.movetoplay.data.model.user_apps.UsedTimeBody
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.model.UsedTime
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.pref.Pref
import com.movetoplay.screens.ChildLockActivity
import com.movetoplay.util.parseArray
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AccessibilityService : AccessibilityService() {

    private var usedTime = 0L
    private val usedTimeDay = 24 * 60 * 60L
    private var timer: CountDownTimer? = null
    private var runningApp: UserApp = UserApp()
    private var userApps = ArrayList<UserApp>()
    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var api: UserAppsRepository

    override fun onServiceConnected() {
        Log.e("onServiceConnected", "onServiceConnected: " + AccessibilityPrefs.currentDay)
        AccessibilityPrefs.currentDay = getTimeMillis()
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
        Log.e("access", "Event: ${event.action}, ${event.eventType}" )

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }
        if (event.packageName == null) {
            Log.e("access", "Event package is NULL")
            return
        }
        if ("com.android.vending" == event.packageName && !AccessibilityPrefs.isPinConfirmed) {
            blockGooglePlay()
            return
        } else if ("com.android.vending" != event.packageName) {
            AccessibilityPrefs.isPinConfirmed = false
        }

        if ("com.android.packageinstaller"==event.packageName && !AccessibilityPrefs.isPinConfirmed){
            blockGooglePlay()
            return
        } else if ("com.android.packageinstaller" != event.packageName) {
            AccessibilityPrefs.isPinConfirmed = false
        }
        checkDay()
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

        Log.e(
            "access",
            "---------------------------------------End----------------------------------- ",
        )
    }

    private fun syncData(app: UserApp) {
        Log.e("access", "syncData appName = ${app.name} usedTime: $usedTime")
        val localUsedTime = AccessibilityPrefs.getUsedTimeById(app.id)
        if (localUsedTime != null) {
            localUsedTime.usedTime += usedTime
            AccessibilityPrefs.setUsedTimeById(app.id, localUsedTime)
        } else AccessibilityPrefs.setUsedTimeById(app.id, UsedTime(app.id, usedTime))
        usedTime = 0L
        runningApp = UserApp()
        serviceScope.launch {
            val usedApp = AccessibilityPrefs.getUsedTimeById(app.id)
            if (usedApp != null) {
                val min: Long = TimeUnit.MILLISECONDS.toMinutes(usedApp.usedTime)
                if (min != 0L) {
                    try {
                        val result = api.setAppUsedTime(app.id, UsedTimeBody(min.toInt()))
                        if (result) {
                            AccessibilityPrefs.clearUsedTimeById(app.id)
                        }
                        Log.e("access", "syncData: $result")
                    } catch (e: Throwable) {
                        Log.e("access", "syncData error: ${e.message}")
                    }
                }
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
        Log.e("access", "blockGooglePlay: ")
        val intent = Intent(this, LockScreenActivity::class.java)
        intent.putExtra("tag", "check")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun checkDay() {
        val diff = AccessibilityPrefs.currentDay - getTimeMillis()
        if (TimeUnit.MILLISECONDS.toDays(diff) < 0 || TimeUnit.MILLISECONDS.toDays(diff) > 0) {
            Log.e("TAG", "checkDay: DNI OTLICHAYUTSYA : " + TimeUnit.MILLISECONDS.toDays(diff))
            AccessibilityPrefs.currentDay = getTimeMillis()
            AccessibilityPrefs.remainingTime = AccessibilityPrefs.dailyLimit
            ExercisesPref.isExtraTimeUsed = false
        } else Log.e(
            "TAG",
            "DAY IS SAME : " + TimeUnit.MILLISECONDS.toDays(diff) + " second: " + TimeUnit.MILLISECONDS.toSeconds(
                diff
            )
        )
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}