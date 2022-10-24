package com.movetoplay.screens.forgot_password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.databinding.ActivityForgotPasswordBinding
import com.movetoplay.pref.Pref

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        initListeners()
    }

    private fun initListeners() {
        binding.btnEnter.setOnClickListener {
            val email = binding.email.text.toString()
            Log.e("emailForgot", email)
            viewModel.getAccountId(email)
        }
        viewModel.mutableLiveDataAccountId.observe(this) {
            if (it) {
                startActivity(Intent(this, GetSmsActivity::class.java))
            } else {
                Toast.makeText(this, Pref.toast, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorHandle.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}
