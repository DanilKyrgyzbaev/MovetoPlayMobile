package com.movetoplay.pref

import com.movetoplay.App

object Pref {
    private val sharedPreferences
        get() = App.sharedPrefs

    var localPassword: String
        get() = sharedPreferences.getString("pin", "") ?: ""
        set(value) = sharedPreferences.edit().putString("pin", value).apply()

    var isFirst: Boolean
        get() = sharedPreferences.getBoolean("isFir", true)
        set(value) = sharedPreferences.edit().putBoolean("isFir", value).apply()

    var userLogin: String
        get() = sharedPreferences.getString("login", "").toString()
        set(value) = sharedPreferences.edit().putString("login", value).apply()

    var userLoginPassword: String
        get() = sharedPreferences.getString("loginPassword", "").toString()
        set(value) = sharedPreferences.edit().putString("loginPassword", value).apply()

    var parentsLogin: String
        get() = sharedPreferences.getString("parentsLogin", "").toString()
        set(value) = sharedPreferences.edit().putString("parentsLogin", value).apply()

    var childName: String
        get() = sharedPreferences.getString("childName", "").toString()
        set(value) = sharedPreferences.edit().putString("childName", value).apply()

    var childAge: String
        get() = sharedPreferences.getString("childAge", "").toString()
        set(value) = sharedPreferences.edit().putString("childAge", value).apply()

    var playingSports: Boolean
        get() = sharedPreferences.getBoolean("playingSports", false)
        set(value) = sharedPreferences.edit().putBoolean("playingSports", value).apply()


    var gender: String
        get() = sharedPreferences.getString("gender", "").toString()
        set(value) = sharedPreferences.edit().putString("gender", value).apply()

    var toast: String
        get() = sharedPreferences.getString("toast", "").toString()
        set(value) = sharedPreferences.edit().putString("toast", value).apply()

    var either_new_or_old: String
        get() = sharedPreferences.getString("either_new_or_old", "").toString()
        set(value) = sharedPreferences.edit().putString("either_new_or_old", value).apply()

    //child's name

    var childId: String
        get() = sharedPreferences.getString("childId", "").toString()
        set(value) = sharedPreferences.edit().putString("childId", value).apply()

    var parentId: String
        get() = sharedPreferences.getString("id", "").toString()
        set(value) = sharedPreferences.edit().putString("id", value).apply()

    var userToken: String
        get() = sharedPreferences.getString("userToken", "").toString()
        set(value) = sharedPreferences.edit().putString("userToken", value).apply()

    var childToken: String
        get() = sharedPreferences.getString("childToken", "").toString()
        set(value) = sharedPreferences.edit().putString("childToken", value).apply()

    var isChild: Boolean
        get() = sharedPreferences.getBoolean("isChild", false)
        set(value) = sharedPreferences.edit().putBoolean("isChild", value).apply()

    var deviceId:String
        get() = sharedPreferences.getString("userDevice", "").toString()
        set(value) = sharedPreferences.edit().putString("userDevice", value).apply()
}