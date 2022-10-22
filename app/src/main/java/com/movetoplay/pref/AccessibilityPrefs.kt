package com.movetoplay.pref

import com.movetoplay.App
import java.util.*
import kotlin.collections.HashSet

object AccessibilityPrefs {
    private val sharedPreferences
        get() = App.sharedPrefs

    var dailyLimit: Long
        get() = sharedPreferences.getLong("daily_limit", 60000)
        set(value) = sharedPreferences.edit().putLong("daily_limit", value).apply()

    var remainingTime: Long
        get() = sharedPreferences.getLong("remaining_limit", dailyLimit)
        set(value) = sharedPreferences.edit().putLong("remaining_limit", value).apply()

    var currentDay: Long
        get() = sharedPreferences.getLong("current_day", System.currentTimeMillis())
        set(value) = sharedPreferences.edit()
            .putLong("current_day", Calendar.getInstance().timeInMillis).apply()

    var limitedApps: HashSet<String>
        get() = sharedPreferences.getStringSet("limited_apps", HashSet<String>()) as HashSet<String>
        set(value) = sharedPreferences.edit().putStringSet("limited_apps", value).apply()

    fun getLimitedAppsById(id: String) =
        sharedPreferences.getStringSet(id, HashSet<String>()) as HashSet<String>

    fun setLimitedAppsById(id: String, value: HashSet<String>) =
        sharedPreferences.edit().putStringSet(id, value).apply()

    var isTimerRunning: Boolean
        get() = sharedPreferences.getBoolean("is_limited", false)
        set(value) = sharedPreferences.edit().putBoolean("is_limited", value).apply()

    var lastPackage: String?
        get() = sharedPreferences.getString("last_package", "")
        set(value) = sharedPreferences.edit().putString("last_package", value).apply()
}