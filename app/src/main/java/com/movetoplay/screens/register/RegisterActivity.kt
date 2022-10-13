package com.movetoplay.screens.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.databinding.ActivityRegisterBinding
import com.movetoplay.model.Registration
import com.movetoplay.pref.Pref
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
        initListeners()

    }
    private fun initListeners() {
        binding.registerButton.setOnClickListener {
            val email: String = binding.email.text.toString().trim()
            val password: String = binding.password.text.toString().trim()
            val age: String = binding.childAge.text.toString().trim()
            if (ValidationUtil.isValidEmail(this,email)&& ValidationUtil.isValidPassword(this,password)&& age.isNotEmpty()){
                viewModel.sendUser(Registration(email,password,age.toInt()),this)
            }
            viewModel.mutableLiveData.observe(this){
                if (it){
                    if (Pref.userToken.isNotEmpty()){
                        if (binding.checkBoxPrivacyPolicy.isChecked){
                            startActivity(Intent(this, SetupProfileActivity::class.java))
                        } else {
                            Toast.makeText(this, "Примите политику конфиденциальности", Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(this, Pref.toast, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        viewModel.errorHandle.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    internal class UpdateTimeTask : TimerTask() {
        override fun run() {
            //создание уведомления
//            create_notification()
        }
    }
}