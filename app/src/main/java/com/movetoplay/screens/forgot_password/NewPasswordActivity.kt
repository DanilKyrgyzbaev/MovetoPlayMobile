package com.movetoplay.screens.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.movetoplay.databinding.ActivityNewPasswordBinding

class NewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}