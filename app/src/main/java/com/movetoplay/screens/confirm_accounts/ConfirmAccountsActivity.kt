package com.movetoplay.screens.confirm_accounts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import com.movetoplay.R
import com.movetoplay.databinding.ActivityConfirmAccountsBinding
import com.movetoplay.model.AccountsConfirm
import com.movetoplay.pref.Pref
import com.movetoplay.screens.create_child_profile.SetupProfileActivity

class ConfirmAccountsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmAccountsBinding
    lateinit var viewModel: ConfirmAccountsViewModel
    var otpCode: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ConfirmAccountsViewModel::class.java)
        val view = findViewById<SmsConfirmationView>(R.id.sms_code_viewС)

        view.onChangeListener = SmsConfirmationView.OnChangeListener { code, isComplete ->
            if (isComplete) {
                otpCode = code.toInt()
            }
        }
        view.startListeningForIncomingMessages()

        initListeners()
    }

    private fun initListeners() {
        binding.btnEnter.setOnClickListener {
            viewModel.confirmAccounts(AccountsConfirm(otpCode!!.toInt()))
        }

        viewModel.mutableLiveData.observe(this) {
            if (it) {
                startActivity(Intent(this, SetupProfileActivity::class.java))
            } else {
                Toast.makeText(this, Pref.toast, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorHandle.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}
