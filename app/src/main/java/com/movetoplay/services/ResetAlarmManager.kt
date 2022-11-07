package com.movetoplay.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.CallSuper
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.pref.AccessibilityPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*


abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
    }
}

@AndroidEntryPoint
class ResetAlarmManager : HiltBroadcastReceiver() {

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + job)

    lateinit var api: UserAppsRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_DATE_CHANGED -> {
            }
            Intent.ACTION_BOOT_COMPLETED -> {
            }
        }
        resetData()
    }

    private fun resetData() {
        Log.e("alarm", "resetData: ${System.currentTimeMillis()}")
        AccessibilityPrefs.remainingTime = AccessibilityPrefs.dailyLimit
        Log.e("alarm", "remaining time: ${AccessibilityPrefs.remainingTime}")
    }

    fun setAlarm(context: Context) {
        Log.e("alarm", "setAlarm: ")
        val requestCode = 123
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ResetAlarmManager::class.java)
        val pi = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Устанавливаем интервал срабатывания в 24:00.
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)

        am.setInexactRepeating(
            AlarmManager.RTC,
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
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as (AlarmManager)
        alarmManager.cancel(sender)
        //Отменяем будильник, связанный с интентом данного класса
    }

}