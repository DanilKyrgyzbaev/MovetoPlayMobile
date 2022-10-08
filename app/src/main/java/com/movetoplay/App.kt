package com.movetoplay

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    companion object {
        lateinit var context: Context
        private var instance: App? = null

        fun getInstance(): App = instance!!
        lateinit var sheredPrefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        sheredPrefs= getSharedPreferences("com.movetoplay", Context.MODE_PRIVATE)
    }
}