package com.movetoplay.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.movetoplay.R
import com.movetoplay.screens.applock.LockScreenActivity

class ChildLockActivity : AppCompatActivity() {

    private lateinit var btnStartExercise: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_lock)

        initViews()
        initListeners()
    }

    private fun initViews() {
        btnStartExercise = findViewById(R.id.btn_start_exercise)
    }

    private fun initListeners() {
        btnStartExercise.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}