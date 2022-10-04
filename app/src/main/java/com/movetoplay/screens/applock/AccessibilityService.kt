package com.movetoplay.screens.applock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import java.io.IOException


class AccessibilityService : AccessibilityService() {

    private val unlockedApps = HashMap<String, Long>()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        var blackList: HashSet<String> = HashSet()


        val prefs = applicationContext.getSharedPreferences("SHARED_PREFS_FILE", MODE_PRIVATE)

        try {
            blackList = prefs.getStringSet("LimitApps", HashSet<String>()) as HashSet<String>
            Log.e("PrefsCome", "onAccessibilityEvent: $blackList")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        val i: Iterator<String> = blackList.iterator()

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val appPackageName = event.packageName.toString()

        if (!appPackageName.equals(packageName.toString(), ignoreCase = true)) {

            while (i.hasNext()) {
                if (i.next() == appPackageName) {

//                    Log.e("Lock", "onAccessibilityEvent: Locked " + i.next())
                    val time = System.currentTimeMillis()


                    if (!unlockedApps.containsKey(appPackageName) || time > (unlockedApps[appPackageName]
                            ?: 0)
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

                        val intent = Intent(this, LockScreenActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Log.d("ololo", "NOOOOOOOOOOOT")
                    }
                } else {
                    Log.d("ololo", "onAccessibilityEvent not locked")
//                Log.d("onAccessibilityEvent not locked")
                }
            }
        }
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