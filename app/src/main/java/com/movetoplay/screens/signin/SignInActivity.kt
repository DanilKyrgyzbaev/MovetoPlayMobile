package com.movetoplay.screens.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.databinding.ActivitySignInBinding
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.screens.forgot_password.ForgotPasswordActivity
import com.movetoplay.screens.parent.MainActivityParent
import com.movetoplay.util.ValidationUtil
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.btnEnter.setOnClickListener {
            val email: String = binding.email.text.toString().trim()
            val password: String = binding.password.text.toString().trim()
            if (ValidationUtil.isValidEmail(this, email) && ValidationUtil.isValidPassword(
                    this,
                    password
                )
            ) {
                Pref.isChild = binding.checkBox.isChecked
                viewModel.login(this, email, password)
            }
        }

        viewModel.resultStatus.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.btnEnter.isClickable = false
                    binding.pbSignIn.visible(true)
                }
                is ResultStatus.Success -> {
                    binding.pbSignIn.visible(false)
                    binding.btnEnter.isClickable = true
                    if (Pref.userAccessToken.isNotEmpty()) {
                        if (Pref.isChild) {
                            startActivity(Intent(this, SetupProfileActivity::class.java))
                        } else {
                            startActivity(Intent(this, MainActivityParent::class.java))
                        }
                        finishAffinity()
                    }
                }
                is ResultStatus.Error -> {
                    binding.pbSignIn.visible(false)
                    binding.btnEnter.isClickable = true
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}