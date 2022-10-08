package com.movetoplay.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.movetoplay.databinding.ActivitySignInBinding
import com.movetoplay.pref.Pref
import com.movetoplay.screens.applock.LimitationAppActivity

class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun initListeners(){
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()
        Pref.userLogin = binding.email.text.toString()
        Log.e("Pref", Pref.userLogin)
        binding.btnEnter.setOnClickListener{
            Pref.userLogin = binding.email.text.toString()
            if (email.isBlank()||password.isBlank()){
                if (binding.checkBox.isChecked){
                    Pref.userLogin = binding.email.text.toString()
                    startActivity(
                        Intent(
                            applicationContext,
                            SetupProfileActivity::class.java
                        )
                    )
                } else {
                    Pref.parentsLogin = binding.email.text.toString()
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

//    fun isEntryValid( role: String, name: String, lastName: String, age: Int): Boolean {
//        if (role.isBlank()||name.isBlank() || lastName.isBlank() || age.toString().isBlank()) {
//            return false
//        }
//        return true
//    }
}