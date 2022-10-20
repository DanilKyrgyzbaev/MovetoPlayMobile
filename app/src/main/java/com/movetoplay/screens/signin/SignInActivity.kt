package com.movetoplay.screens.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.databinding.ActivitySignInBinding
import com.movetoplay.domain.model.User
import com.movetoplay.pref.Pref
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.screens.parent.MainActivityParent
import com.movetoplay.util.ValidationUtil


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        initListeners()
    }
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun initListeners() {
        Log.e("Pref", Pref.userLogin)
        binding.btnEnter.setOnClickListener {
            val email: String = binding.email.text.toString().trim()
            val password: String = binding.password.text.toString().trim()
            if (ValidationUtil.isValidEmail(this, email) && ValidationUtil.isValidPassword(
                    this,
                    password
                )
            ) {
                viewModel.isChild.value = binding.checkBox.isChecked
                viewModel.sendUser(User(email, password))
            }
        }
        viewModel.mutableLiveData.observe(this) {
            if (it) {
                if (Pref.userToken.isNotEmpty()) {
                    if (binding.checkBox.isChecked) {
                        Pref.isChild = true
                        startActivity(Intent(this, SetupProfileActivity::class.java))
                    } else {
                        startActivity(Intent(this, MainActivityParent::class.java))
                        Pref.isChild = false
                    }
                    finishAffinity()
                } else {
                    Toast.makeText(this, Pref.toast, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.errorHandle.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}