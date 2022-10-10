package com.movetoplay.screens.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.databinding.ActivitySignInBinding
import com.movetoplay.model.User
import com.movetoplay.pref.Pref
import com.movetoplay.screens.applock.LimitationAppActivity
import com.movetoplay.screens.create_child_profile.SetupProfileActivity

class SignInActivity: AppCompatActivity() {
    val MIN_PASSWORD_LENGTH = 6
    private lateinit var binding: ActivitySignInBinding
    lateinit var viewModel: SigninViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SigninViewModel::class.java)

        initListeners()
    }
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    private fun initListeners(){
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()
//        Pref.userLogin = binding.email.text.toString()
        Log.e("Pref", Pref.userLogin)
        binding.btnEnter.setOnClickListener{
//            Pref.userLogin = binding.email.text.toString()
            if (email.isBlank()||password.isBlank()){
                Pref.userLogin = binding.email.text.toString()
                Pref.userLoginPassword = binding.password.text.toString()
                viewModel.sendUser(User(Pref.userLogin,Pref.userLoginPassword),this)
                if (binding.checkBox.isChecked){
//                    Pref.userLogin = binding.email.text.toString()
                    startActivity(
                        Intent(
                            applicationContext,
                            SetupProfileActivity::class.java
                        )
                    )
                } else {
//                    Pref.parentsLogin = binding.email.text.toString()
                    Log.e("parentsLogin", Pref.parentsLogin)
                    startActivity(
                        Intent(
                            applicationContext,
                            LimitationAppActivity::class.java
                        )
                    )
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}