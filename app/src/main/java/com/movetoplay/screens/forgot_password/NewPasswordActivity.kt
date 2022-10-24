package com.movetoplay.screens.forgot_password // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.databinding.ActivityNewPasswordBinding
import com.movetoplay.model.ChangePasswordByCode
import com.movetoplay.pref.Pref
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.screens.signin.SignInActivity

class NewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPasswordBinding
    lateinit var viewModel: ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        setContentView(binding.root)
        initListeners()
    }
    private fun initListeners() {
        binding.btnEnter.setOnClickListener {
            val newPassword = binding.password.text.toString().trim()
            val confirmPassword = binding.password2.text.toString().trim()
            if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    viewModel.changePasswordByCode(ChangePasswordByCode(confirmPassword))
                } else {
                    Toast.makeText(this, "Пароли не совподает", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.mutableLiveDataPasswordByCode.observe(this) {
            if (it) {
                startActivity(Intent(this, SignInActivity::class.java))
            } else {
                Toast.makeText(this, Pref.toast, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorHandle.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}
