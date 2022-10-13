package com.movetoplay.screens.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.databinding.ActivityRegisterBinding
import com.movetoplay.model.Registration
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.util.ValidationUtil
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.registerButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val age = binding.childAge.text.toString()

            if (ValidationUtil.isValidEmail(this, email)|| ValidationUtil.isValidPassword(this, password) || age.isEmpty()) {
                viewModel.sendUser(Registration(email, password, age.toInt()), this)
                startActivity(Intent(applicationContext, SetupProfileActivity::class.java))
            }
        }
    }

    internal class UpdateTimeTask : TimerTask() {
        override fun run() {
            //создание уведомления
//            create_notification()
        }
    }
}