package com.movetoplay.screens.confirm_accounts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import com.movetoplay.databinding.ActivityConfirmAccountsBinding
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.screens.parent.MainActivityParent
import com.movetoplay.util.ValidationUtil
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmAccountsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmAccountsBinding
    private val viewModel: ConfirmAccountsViewModel by viewModels()
    private var otpCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                Log.e("reg", "initListeners: $otpCode" )
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
                            startActivity(Intent(this, MainActivityParent::class.java))
                        }
                        finishAffinity()
                    }
                }
                is ResultStatus.Error -> {
                    binding.progress.visible(false)
                    binding.btnEnter.isClickable = true
                    Log.e("reg", "initListeners: ${it.error}", )
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
