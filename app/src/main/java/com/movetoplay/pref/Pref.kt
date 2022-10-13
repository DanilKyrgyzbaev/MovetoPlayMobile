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

    var userLoginPassword: String
        get() = sheredPreferences.getString("loginPassword","").toString()
        set(value) = sheredPreferences.edit().putString("loginPassword", value).apply()

    var parentsLogin: String
        get() = sheredPreferences.getString("parentsLogin","").toString()
        set(value) = sheredPreferences.edit().putString("parentsLogin", value).apply()

    var childName: String
        get() = sheredPreferences.getString("childName","").toString()
        set(value) = sheredPreferences.edit().putString("childName", value).apply()

    var childAge: String
        get() = sheredPreferences.getString("childAge","").toString()
        set(value) = sheredPreferences.edit().putString("childAge", value).apply()

    var playingSports: Boolean
        get() = sheredPreferences.getBoolean("playingSports",false)
        set(value) = sheredPreferences.edit().putBoolean("playingSports",value).apply()


    var gender: String
        get() = sheredPreferences.getString("gender","").toString()
        set(value) = sheredPreferences.edit().putString("gender", value).apply()

    var toast: String
        get() = sheredPreferences.getString("toast","").toString()
        set(value) = sheredPreferences.edit().putString("toast", value).apply()

    var either_new_or_old: String
        get() = sheredPreferences.getString("either_new_or_old","").toString()
        set(value) = sheredPreferences.edit().putString("either_new_or_old", value).apply()

    //child's name

    var childId: String
        get() = sheredPreferences.getString("childId","").toString()
        set(value) = sheredPreferences.edit().putString("childId", value).apply()

    var userToken: String
        get() = sheredPreferences.getString("userToken","").toString()
        set(value) = sheredPreferences.edit().putString("userToken", value).apply()

}