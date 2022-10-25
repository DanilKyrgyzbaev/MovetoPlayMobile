package com.movetoplay.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.CallSuper
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.pref.AccessibilityPrefs
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import javax.inject.Inject

abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
    }
}

@AndroidEntryPoint
class ResetAlarmManager : HiltBroadcastReceiver() {

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + job)

    @Inject
    lateinit var api: UserAppsRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        resetData()
    }

    private fun resetData() {
        Log.e("alarm", "resetData: ${System.currentTimeMillis()}")
        AccessibilityPrefs.remainingTime = AccessibilityPrefs.dailyLimit
        Log.e("alarm", "remaining time: ${AccessibilityPrefs.remainingTime}")
    }

    fun setAlarm(context: Context) {
        Log.e("alarm", "setAlarm: ")
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ResetAlarmManager::class.java)
        val pi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, 0, intent, 0)
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                0
            )
        }
        //Устанавливаем интервал срабатывания в 24:00.

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 24)
            set(Calendar.MINUTE,0)
        }
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pi
        )
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, ResetAlarmManager::class.java)
        val sender =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
              0
            )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as (AlarmManager)
        alarmManager.cancel(sender)
        //Отменяем будильник, связанный с интентом данного класса
    }

    companion object {
        const val ONE_TIME = "oneTime"
    }
}