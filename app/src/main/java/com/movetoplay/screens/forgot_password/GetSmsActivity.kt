package com.movetoplay.screens.forgot_password // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import com.movetoplay.R
import com.movetoplay.databinding.ActivityGetSmsBinding
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.model.JwtSessionToken
import com.movetoplay.pref.Pref
import com.movetoplay.util.ValidationUtil
import com.movetoplay.util.visible
import java.util.*
import java.util.concurrent.TimeUnit

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
        getNewCode()
    }

    fun getNewCode() {
        val duration = TimeUnit.MINUTES.toMillis(1)
        val timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(p0: Long) {
                binding.countDownTimer.visibility = View.VISIBLE
                binding.sendConfCode.visibility = View.GONE
                val sDuration: String = String.format(
                    Locale.ENGLISH,
                    "%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(p0),
                    TimeUnit.MILLISECONDS.toSeconds(p0) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(p0)),
                    TimeUnit.MILLISECONDS.toMinutes(p0)
                )
                binding.countDownTimer.text = sDuration
            }

            override fun onFinish() {
                binding.sendConfCode.visibility = View.VISIBLE
                binding.countDownTimer.visibility = View.GONE
                Toast.makeText(this@GetSmsActivity, "OnFinish", Toast.LENGTH_SHORT).show()
//                binding.sendConfCode.setOnClickListener {
//                    viewModel.resendConfirmCode()
//                }
                viewModel.resultStatusResendConfirmCode.observe(this@GetSmsActivity){
                    when (it) {
                        is ResultStatus.Loading -> {
                            binding.btnEnter.isClickable = false
                        }
                        is ResultStatus.Success -> {
                            binding.btnEnter.isClickable = true
                        }
                        is ResultStatus.Error -> {
                            binding.btnEnter.isClickable = true
                            Log.e("reg", "initListeners: ${it.error}")
                            Toast.makeText(this@GetSmsActivity, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        timer.start()
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
