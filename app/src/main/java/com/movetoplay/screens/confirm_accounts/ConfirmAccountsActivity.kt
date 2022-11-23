package com.movetoplay.screens.confirm_accounts

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import com.movetoplay.databinding.ActivityConfirmAccountsBinding
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.screens.parent.MainParentActivity
import com.movetoplay.util.ValidationUtil
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ConfirmAccountsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmAccountsBinding
    private val viewModel: ConfirmAccountsViewModel by viewModels()
    private var otpCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getNewCode()
        initListeners()
    }

    private fun initListeners() {
        binding.smsCodeView.onChangeListener =
            SmsConfirmationView.OnChangeListener { code, isComplete ->
                if (isComplete) {
                    otpCode = code
                }
            }
        binding.smsCodeView.startListeningForIncomingMessages()

        binding.btnEnter.setOnClickListener {
            if (ValidationUtil.isValidCode(this, otpCode) && Pref.userAccessToken != "") {
                Log.e("reg", "initListeners: $otpCode")
                viewModel.confirmEmail(otpCode!!.toInt())
            }
        }

        viewModel.resultStatus.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.btnEnter.isClickable = false
                    binding.progress.visible(true)
                }
                is ResultStatus.Success -> {
                    binding.progress.visible(false)
                    binding.btnEnter.isClickable = true
                    if (Pref.userAccessToken.isNotEmpty()) {
                        if (Pref.isChild) {
                            startActivity(Intent(this, SetupProfileActivity::class.java))
                        } else {
                            startActivity(Intent(this, MainParentActivity::class.java))
                        }
                        finishAffinity()
                    }
                }
                is ResultStatus.Error -> {
                    binding.progress.visible(false)
                    binding.btnEnter.isClickable = true
                    Log.e("reg", "initListeners: ${it.error}")
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                Toast.makeText(this@ConfirmAccountsActivity, "OnFinish", Toast.LENGTH_SHORT).show()
                binding.sendConfCode.setOnClickListener {
                    viewModel.resendConfirmCode()
                }
                viewModel.resultStatusResendConfirmCode.observe(this@ConfirmAccountsActivity){
                    when (it) {
                        is ResultStatus.Loading -> {
                            binding.btnEnter.isClickable = false
                            binding.progress.visible(true)
                        }
                        is ResultStatus.Success -> {
                            binding.progress.visible(false)
                            binding.btnEnter.isClickable = true
                        }
                        is ResultStatus.Error -> {
                            binding.progress.visible(false)
                            binding.btnEnter.isClickable = true
                            Log.e("reg", "initListeners: ${it.error}")
                            Toast.makeText(this@ConfirmAccountsActivity, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        timer.start()
    }
}
