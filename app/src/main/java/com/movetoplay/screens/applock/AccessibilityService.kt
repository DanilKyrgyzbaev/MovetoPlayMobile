package com.movetoplay.screens.applock

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.movetoplay.screens.ChildLockActivity
import java.io.IOException
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class AccessibilityService : AccessibilityService() {

    private val unlockedApps = HashMap<String, Long>()
    private var blackList: HashSet<String> = HashSet()
    private lateinit var timeDuration: String
    private var time: Long = 0

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        val i: Iterator<String> = getBlackListPrefs().iterator()
        Log.e("AccessTime", "onAccessibilityEvent: " + getTimePrefs().toString())

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }


        val appPackageName = event.packageName.toString()

        if (!appPackageName.equals(packageName.toString(), ignoreCase = true)) {

            while (i.hasNext()) {
                if (i.next() == appPackageName) {
                    
//                    Log.e("Lock", "onAccessibilityEvent: Locked " + i.next())

                    time = System.currentTimeMillis()
                    Log.e("PackName", "onAccessibilityEvent: " + appPackageName)


                    Log.e("EventTime", "Event time: " + time)
                    Log.e("EventTime", "Event time: " + event.eventTime)

                    if (!unlockedApps.containsKey(appPackageName) || time > (unlockedApps[appPackageName] ?: 0)
                    ) {
                        callback = object : Callback {

                            override fun onSuccess() {

//                            val reLockTime = when(appPrefs.getReLockTime()) {
//                                AppLock.TIME_30S -> TimeUnit.SECONDS.toMillis(30)
//                                AppLock.TIME_1M -> TimeUnit.MINUTES.toMillis(1)
//                                AppLock.TIME_5M -> TimeUnit.MINUTES.toMillis(5)
//                                AppLock.TIME_10M -> TimeUnit.MINUTES.toMillis(10)
//                                else -> TimeUnit.MINUTES.toMillis(30)
//                            }

//                            unlockedApps[appPackageName] = System.currentTimeMillis() + reLockTime
                            }

                            override fun onFailed() {
                                performGlobalAction(GLOBAL_ACTION_HOME)
                            }
                        }
                        val timeDuration = getTimePrefs()

                        val timer = object : CountDownTimer(timeDuration, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                setTimePrefs(millisUntilFinished)
                            }
                            override fun onFinish() {
                                openLockScreen()
                            }
                        }
                        timer.start()

                        Log.d("ololo", "NOOOOOOOOOOOT")
                    }
                } else {
                    Log.d("ololo", "onAccessibilityEvent not locked")
//                Log.d("onAccessibilityEvent not locked")
                }
            }
        }
    }

    private fun openLockScreen() {
        val intent = Intent(this, ChildLockActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun getBlackListPrefs(): HashSet<String> {
        val prefs = applicationContext.getSharedPreferences("SHARED_PREFS_FILE", MODE_PRIVATE)
        try {
            blackList = prefs.getStringSet("LimitApps", HashSet<String>()) as HashSet<String>
            Log.e("PrefsCome", "onAccessibilityEvent: $blackList")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return blackList
    }


    fun getTimePrefs(): Long {
        val prefs = applicationContext.getSharedPreferences("time_prefs", MODE_PRIVATE)
        timeDuration = prefs.getString("LimitTime", "60000").toString()

        return timeDuration.toLong()
    }
//    fun getTimePrefs(): Long {
//        val prefs = applicationContext.getSharedPreferences("time_prefs", MODE_PRIVATE)
//        timeDuration = prefs.getString("LimitTime", String()).toString()
//
//        return timeDuration.toLong()
//    }

    fun setTimePrefs(millisRemaining: Long) {
        val prefs: SharedPreferences =
            getSharedPreferences("time_prefs", Context.MODE_PRIVATE)

        val editor: SharedPreferences.Editor = prefs.edit()
        try {
            editor.putString("LimitTime", millisRemaining.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        editor.apply()
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