package com.movetoplay.screens.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.movetoplay.databinding.ActivityGetSmsBinding
import com.movetoplay.databinding.ActivityNewPasswordBinding

class GetSmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetSmsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetSmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}