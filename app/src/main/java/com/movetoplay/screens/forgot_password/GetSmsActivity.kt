package com.movetoplay.screens.forgot_password // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import com.movetoplay.R
import com.movetoplay.databinding.ActivityGetSmsBinding
import com.movetoplay.model.JwtSessionToken
import com.movetoplay.pref.Pref
import com.movetoplay.util.ValidationUtil

class GetSmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetSmsBinding
    lateinit var viewModel: ForgotPasswordViewModel
    var otpCode: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetSmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        val view = findViewById<SmsConfirmationView>(R.id.sms_code_viewGetSms)
        view.onChangeListener = SmsConfirmationView.OnChangeListener { code, isComplete ->
            //  if (isComplete) {
            otpCode = code.toInt()
            //  }
        }
        view.startListeningForIncomingMessages()
        initListeners()
    }

    private fun initListeners() {
        binding.btnEnter.setOnClickListener {
            if (ValidationUtil.isValidCode(this, otpCode.toString())) {
                viewModel.getJwtSessionToken(JwtSessionToken(Pref.accountId, otpCode!!.toInt()))
            }
        }
        viewModel.mutableLiveDataJwtSessionToken.observe(this) {
            if (it) {
                startActivity(Intent(this, NewPasswordActivity::class.java))
            } else {
                Toast.makeText(this, Pref.toast, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorHandle.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}
