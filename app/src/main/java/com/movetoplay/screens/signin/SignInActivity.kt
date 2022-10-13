package com.movetoplay.screens.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.movetoplay.databinding.ActivitySignInBinding
import com.movetoplay.model.ErrorResponse
import com.movetoplay.model.MyCustomError
import com.movetoplay.model.ResponseSucces
import com.movetoplay.model.User
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.Pref
import com.movetoplay.presentation.vm.session_creation.SessionCreationVM
import com.movetoplay.screens.applock.LimitationAppActivity
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.util.ValidationUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity: AppCompatActivity() {
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
        Log.e("Pref", Pref.userLogin)
        binding.btnEnter.setOnClickListener{
            val email: String = binding.email.text.toString()
            val password: String = binding.password.text.toString()
            val token = Pref.userToken
            if (ValidationUtil.isValidEmail(this,email) && ValidationUtil.isValidPassword(this,password)){
                viewModel.sendUser(User(email, password))
                if (token.isNotEmpty()){
                    if (binding.checkBox.isChecked){
                        startActivity(Intent(this,SetupProfileActivity::class.java ))
                    } else {
                        startActivity(Intent(this,LimitationAppActivity::class.java ))
                    }
                } else {
                    Toast.makeText(this, Pref.toast, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}