package com.movetoplay.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.movetoplay.R
import java.io.IOException


class SettingTimeActivity : AppCompatActivity() {

    private lateinit var btnSetTime: Button
    private lateinit var timePicker: TimePicker
    private lateinit var etLimit: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_time)

        initViews()
        initListeners()
    }

    private fun initViews() {
        btnSetTime = findViewById(R.id.buttonAlarm)
        etLimit = findViewById(R.id.et_limit)
    }

    private fun initListeners() {
        btnSetTime.setOnClickListener {
            val time: String = etLimit.text.toString()

            val timeInMilliseconds = time.toLong() * 60000

            val prefs: SharedPreferences =
                getSharedPreferences("time_prefs", Context.MODE_PRIVATE)

            val editor: SharedPreferences.Editor = prefs.edit()
            try {
                editor.putString("LimitTime", timeInMilliseconds.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }
            editor.apply()

            finish()
        }
    }
}