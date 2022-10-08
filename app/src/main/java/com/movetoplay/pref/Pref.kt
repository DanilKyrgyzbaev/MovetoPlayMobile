package com.movetoplay.pref

import com.movetoplay.App

object Pref {
    private val sheredPreferences
    get() = App.sheredPrefs

    var localPassword: String
        get() = sheredPreferences.getString("pin","") ?: ""
        set(value) = sheredPreferences.edit().putString("pin",value).apply()

    var isFirst: Boolean
        get() = sheredPreferences.getBoolean("isFir",true)
        set(value) = sheredPreferences.edit().putBoolean("isFir",value).apply()

    var userLogin: String
        get() = sheredPreferences.getString("login","").toString()
        set(value) = sheredPreferences.edit().putString("login", value).apply()

    var parentsLogin: String
        get() = sheredPreferences.getString("parentsLogin","").toString()
        set(value) = sheredPreferences.edit().putString("parentsLogin", value).apply()

    var childName: String
        get() = sheredPreferences.getString("childName","").toString()
        set(value) = sheredPreferences.edit().putString("childName", value).apply()

    var childAge: Int
        get() = sheredPreferences.getInt("childAge",0)
        set(value) = sheredPreferences.edit().putInt("childAge", value).apply()

    var playingSports: Boolean
        get() = sheredPreferences.getBoolean("playingSports",true)
        set(value) = sheredPreferences.edit().putBoolean("playingSports",value).apply()

    var gender: Int
        get() = sheredPreferences.getInt("gender", -1)
        set(value) = sheredPreferences.edit().putInt("gender", value).apply()

    //child's name

    var userId: Int
        get() = sheredPreferences.getInt("id",0)
        set(value) = sheredPreferences.edit().putInt("id", value).apply()

    var userToken: String
        get() = sheredPreferences.getString("userToken","").toString()
        set(value) = sheredPreferences.edit().putString("userToken", value).apply()

}