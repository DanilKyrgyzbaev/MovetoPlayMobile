package com.movetoplay.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movetoplay.App
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.model.UsedTime
import com.movetoplay.util.parseArray
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object AccessibilityPrefs {
    private val sharedPreferences
        get() = App.sharedPrefs

    const val remainingTimeKey = "remaining-time"

    val getSharedPreferences = sharedPreferences

    var dailyLimit: Long
        get() = sharedPreferences.getLong("daily_limit", 60*60000)
        set(value) = sharedPreferences.edit().putLong("daily_limit", value).apply()

    var remainingTime: Long
        get() = sharedPreferences.getLong(remainingTimeKey, dailyLimit)
        set(value) = sharedPreferences.edit().putLong(remainingTimeKey, value).apply()

    var currentDay: Long
        get() = sharedPreferences.getLong("current_day", System.currentTimeMillis())
        set(value) = sharedPreferences.edit()
            .putLong("current_day", Calendar.getInstance().timeInMillis).apply()

    var limitedApps: HashMap<String, String>
        get() = parseArray(
            sharedPreferences.getString("limited_apps", ""),
            object : TypeToken<HashMap<String, String>>() {}.type
        ) ?: HashMap()
        set(value) = sharedPreferences.edit().putString("limited_apps", Gson().toJson(value))
            .apply()

    fun getLimitedAppsById(id: String) = sharedPreferences.getString(id, "")

    fun setLimitedAppsById(id: String, value: ArrayList<UserApp>) =
        sharedPreferences.edit().putString(id, Gson().toJson(value)).apply()


    var childInfo: ChildInfo
        get() = Gson().fromJson(
            sharedPreferences.getString("child-info", ""),
            ChildInfo::class.java
        ) ?: ChildInfo()
        set(value) = sharedPreferences.edit().putString("child-info", Gson().toJson(value)).apply()

    fun getUsedTimeById(id: String) =
        Gson().fromJson(sharedPreferences.getString(id, ""), UsedTime::class.java) ?: null

    fun setUsedTimeById(id: String, value: UsedTime) =
        sharedPreferences.edit().putString(id, Gson().toJson(value)).apply()

    fun clearUsedTimeById(id: String) {
        sharedPreferences.edit().remove(id).apply()
    }

    var isPinConfirmed: Boolean
        get() = sharedPreferences.getBoolean("is_Pin_confirmed", false);
        set(value) = sharedPreferences.edit().putBoolean("is_Pin_confirmed", value).apply()
}
