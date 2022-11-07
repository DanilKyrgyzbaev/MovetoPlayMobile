package com.movetoplay.screens.applock

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.screens.SettingTimeActivity
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint
import com.movetoplay.databinding.ActivityLimitationAppBinding
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.pref.Pref

@AndroidEntryPoint
class LimitationAppActivity : AppCompatActivity() {

    private lateinit var adapter: LimitationsAppsAdapter
    private lateinit var binding: ActivityLimitationAppBinding
    private var userApps = ArrayList<UserApp>()
    private var child = Child()

    private val vm: LimitationAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLimitationAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initListeners()
    }

    private fun initViews() {
        val argument = intent.getStringExtra("child")
        if (!argument.isNullOrEmpty()) {
            child = Gson().fromJson(argument, Child::class.java)
            child.id.let { vm.getLimited(it) }
        } else Toast.makeText(this, "Профиль ребенка не найден!", Toast.LENGTH_LONG).show()

        adapter = LimitationsAppsAdapter(userApps)
        binding.rvLimitations.adapter = adapter
    }

    private fun initListeners() {
        binding.apply {
            btnFinish.setOnClickListener {
                saveBeforeFinish()
            }

            imgTimeSettings.setOnClickListener {
                val intent = Intent(this@LimitationAppActivity, SettingTimeActivity::class.java)
                startActivity(intent)
            }

            imgSetPin.setOnClickListener {
                val intent = Intent(this@LimitationAppActivity, LockScreenActivity::class.java)
                startActivity(intent)
            }
        }

        vm.userApps.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.pbLimitation.visible(true)
                }
                is ResultStatus.Error -> {
                    binding.pbLimitation.visible(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
                is ResultStatus.Success -> {
                    binding.pbLimitation.visible(false)
                    userApps = it.data as ArrayList<UserApp>
                    if (userApps.isNotEmpty())
                        setData(userApps)
                    else Toast.makeText(
                        this,
                        "Список пуст! Сделайте вход с устройства ребенка",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        vm.loading.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.pbLimitation.visible(true)
                    Toast.makeText(this, "Данные сохраняются...", Toast.LENGTH_SHORT).show()
                }
                is ResultStatus.Error -> {
                    binding.pbLimitation.visible(false)
                    Toast.makeText(
                        this,
                        " Ошибка при сохранении, попробуйте еще раз",
                        Toast.LENGTH_SHORT
                    ).show()
                    goTo()
                }
                is ResultStatus.Success -> {
                    binding.pbLimitation.visible(false)
                    Toast.makeText(this, "Данные успешно сохранились!", Toast.LENGTH_SHORT).show()
                    goTo()
                }
            }
        }
    }

    private fun setData(userApps: ArrayList<UserApp>) {
        userApps.forEachIndexed { index, app ->
            userApps[index].drawable = ApkInfoExtractor(this).getAppIconByPackageName(app.packageName)
        }
       adapter.updateList(userApps)
    }

    private fun saveBeforeFinish() {
        val appsLimit = adapter.getBlockedApps()
        if (appsLimit.isNotEmpty())
            vm.setLimits(appsLimit)
        else goTo()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goTo()
    }

    private fun goTo() {
        finish()
    }
}