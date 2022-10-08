package com.movetoplay.presentation.ui.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.movetoplay.R
import com.movetoplay.pref.Pref
import com.movetoplay.screens.Auth
import com.movetoplay.screens.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            checkUser()
            finish()
        }, 3000)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
    }

    private fun checkUser() {
        if (Pref.userLogin.isEmpty()) {
            val intent = Intent(this, Auth::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}