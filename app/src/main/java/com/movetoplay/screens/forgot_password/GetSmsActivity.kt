package com.movetoplay.screens.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import com.movetoplay.databinding.ActivityGetSmsBinding
import com.movetoplay.databinding.ActivityNewPasswordBinding

class GetSmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetSmsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetSmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnEnter.setOnClickListener {
            binding.smsCodeView.onChangeListener = SmsConfirmationView.OnChangeListener { code, isComplete ->
                Toast.makeText(this, "$code $isComplete", Toast.LENGTH_SHORT).show()
                Log.e("code", code)
            }
            binding.smsCodeView.startListeningForIncomingMessages()
        }
    }
}