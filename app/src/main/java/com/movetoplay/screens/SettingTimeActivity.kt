package com.movetoplay.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.movetoplay.R
import com.movetoplay.pref.AccessibilityPrefs

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

            AccessibilityPrefs.dailyLimit = timeInMilliseconds

            finish()
        }
    }
}
