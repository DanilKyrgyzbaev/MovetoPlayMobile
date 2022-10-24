package com.movetoplay.screens.register

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.movetoplay.databinding.ActivityRegisterBinding
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.screens.confirm_accounts.ConfirmAccountsActivity
import com.movetoplay.util.ValidationUtil
import com.movetoplay.util.visible
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.AndroidEntryPoint

import java.util.*

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val NOTIFY_ID = 101
    private val CHANNEL_ID = "MoveToPlay"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()

        // Creating an extended library configuration.
        // Creating an extended library configuration.
        val config =
            YandexMetricaConfig.newConfigBuilder("e5a90bfc-abfd-4c35-a47b-308effb3c011").build()
        // Initializing the AppMetrica SDK.
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)
        // Automatic tracking of user activity.
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(application)

    }

    private fun initListeners() {
        binding.registerButton.setOnClickListener {
            val email: String = binding.email.text.toString().trim()
            val password: String = binding.password.text.toString().trim()
            val age: String = binding.childAge.text.toString().trim()
            if (ValidationUtil.isValidEmail(this, email) && ValidationUtil.isValidPassword(
                    this,
                    password
                ) && age.isNotEmpty()
            ) {
                if (binding.checkBoxPrivacyPolicy.isChecked)
                    viewModel.register(email, password, age)
                else Toast.makeText(
                    this,
                    "Примите политику конфиденциальности",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.resultStatus.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.registerButton.isClickable = false
                    binding.progress.visible(true)
                }
                is ResultStatus.Success -> {
                    binding.progress.visible(false)
                    startActivity(Intent(this, ConfirmAccountsActivity::class.java))
                }
                is ResultStatus.Error -> {
                    binding.progress.visible(false)
                    binding.registerButton.isClickable = true
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun createNotification() {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_overlay)
                .setContentTitle("Время кончилось")
                .setContentText("Нужно пройти испытания, чтобы добавить времени")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )
        notificationManager.notify(NOTIFY_ID, builder.build())
    }

    fun startTimer() {
        val timer = Timer()
        timer.schedule(UpdateTimeTask(), 0, 1000) //установленно в данный момент на 1 секунду
    }

    inner class UpdateTimeTask : TimerTask() {
        override fun run() {
            //создание уведомления
            createNotification()
        }
    }
}