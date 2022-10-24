package com.movetoplay.screens.applock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.google.gson.reflect.TypeToken
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.Pref
import com.movetoplay.screens.ChildLockActivity
import com.movetoplay.services.ResetAlarmManager
import com.movetoplay.util.parseArray
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.collections.HashMap

@AndroidEntryPoint
class AccessibilityServiceVersion2 : AccessibilityService() {

    @Inject
    lateinit var apiRepository: UserAppsRepository
    private var timer: CountDownTimer? = null
    private val serviceJob = Job()
    //private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var userApps = ArrayList<UserApp>()
    private lateinit var resetAlarmManager: ResetAlarmManager

    override fun onServiceConnected() {
        Log.e("onServiceConnected", "onServiceConnected: " + AccessibilityPrefs.currentDay)
        // AccessibilityPrefs.currentDay = System.currentTimeMillis()

        resetAlarmManager = ResetAlarmManager()
        resetAlarmManager.setAlarm(this.applicationContext)

        updateUserApps()

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
         updateUserApps()

        val eventPackage = event.packageName.toString()
        Log.e("access", "eventPackageName  $eventPackage")
        if (!eventPackage.equals(
                packageName.toString(),
                ignoreCase = true
            )
        ) {
            if (AccessibilityPrefs.lastPackage == "") {
                userApps.forEach { app ->
                    Log.e("access", "Event app package: ${app.packageName} type: ${app.type}")
                    if (app.packageName == eventPackage && app.type == "allowed") return
                    if (app.packageName == eventPackage && app.type == "unallowed") {
                        Log.e("access", "Is timer running: ${AccessibilityPrefs.isTimerRunning}")
                        if (!AccessibilityPrefs.isTimerRunning) {
                            AccessibilityPrefs.isTimerRunning = true
                            AccessibilityPrefs.lastPackage = eventPackage
                            startTimer()
                        }
                        return
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

    private fun openLockScreen() {
        Log.e("access", "openLockScreen: " )
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
        return AccessibilityPrefs.remainingTime
    }

    private fun blockGooglePlay() {
        val intent = Intent(this, LockScreenChildActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()

        resetAlarmManager.cancelAlarm(this.applicationContext)
        serviceJob.cancel()
    }
}