package com.movetoplay.screens.applock

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.google.gson.Gson
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.screens.ChildLockActivity
import io.ktor.util.date.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class AccessibilityService : AccessibilityService() {

    private var blackList: HashSet<String> = HashSet()
    private var timeDuration: Long = 0

    override fun onServiceConnected() {
        Log.e("onServiceConnected", "onServiceConnected: " + AccessibilityPrefs.currentDay)
        AccessibilityPrefs.currentDay = System.currentTimeMillis()
        super.onServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Log.e("AccessTime", "onAccessibilityEvent: " + getRemainingTimePrefs().toString())

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }
        checkDay()

        val appPackageName = event.packageName.toString()
        if (!appPackageName.equals(packageName.toString(), ignoreCase = true)) {
            getLimitedAppsPrefs().forEach {
                if (it == appPackageName) {
                    startTimer(false)
                    Log.e("PackageN", "onAccessibilityEvent: limited list package $it")
                    Log.e("PackageN", "onAccessibilityEvent: package $appPackageName")
                }
            }
        }

        if ("com.android.launcher3" == appPackageName) {
            startTimer(true)
            Log.e("Timer", "Timer Stopped: "+event.eventType)
        }
        Log.e("eventjopa", "onAccessibilityEvent: $appPackageName")

        Log.e("Time", "onAccessibilityEvent: " + getTimeMillis())

        if ("com.android.vending" == packageName) {
            blockGooglePlay()
        }
    }

    private fun checkDay() {
        val diff = AccessibilityPrefs.currentDay - System.currentTimeMillis()
        if (TimeUnit.MILLISECONDS.toDays(diff) < 0) {
            Log.e("TAG", "checkDay: DNI OTLICHAYUTSYA : " + TimeUnit.MILLISECONDS.toDays(diff))
            AccessibilityPrefs.currentDay = System.currentTimeMillis()
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

    var timer: CountDownTimer? = null
    private fun startTimer(exit: Boolean) {
        val timeDuration = getRemainingTimePrefs()
        if (!exit) {
            timer = object : CountDownTimer(timeDuration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    AccessibilityPrefs.remainingTime = millisUntilFinished
                }

                override fun onFinish() {
                    openLockScreen()
                }
            }
            timer?.start()
        } else {
            Log.e("Timer", "remaining time $timer")
            timer?.cancel()
        }
    }

    private fun getLimitedAppsPrefs(): HashSet<String> {
        blackList = AccessibilityPrefs.limitedApps
        Log.e("PrefsCome", "onAccessibilityEvent: $blackList")
        return blackList
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
}