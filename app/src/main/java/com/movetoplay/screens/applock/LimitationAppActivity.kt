package com.movetoplay.screens.applock

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.movetoplay.databinding.ActivityLimitationAppBinding
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.screens.set_time.SettingTimeActivity
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint
import com.movetoplay.pref.AccessibilityPrefs

@AndroidEntryPoint
class LimitationAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLimitationAppBinding
    private lateinit var adapter: LimitationsAppsAdapter
    private var userApps = ArrayList<UserApp>()
    private lateinit var child: ChildInfo
    private val vm: LimitationAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLimitationAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initListeners()
    }

    private fun initViews() {
        val argument = intent.getStringExtra("childInfo")
        if (!argument.isNullOrEmpty()) {
            child = Gson().fromJson(argument, ChildInfo::class.java)
            AccessibilityPrefs.childInfo = child
            child.id.let { vm.getUserApps(it) }
        } else child = AccessibilityPrefs.childInfo
        adapter = LimitationsAppsAdapter(ArrayList())
        binding.rvLimitations.adapter = adapter
    }

    private fun initListeners() {
        binding.apply {
            btnFinish.setOnClickListener {
                if (child.pinCode == null || child.pinCode.toString().isEmpty()) {
                    Toast.makeText(
                        this@LimitationAppActivity,
                        "Установите пин код",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e("Pin-code", AccessibilityPrefs.isPinConfirmed.toString())
                } else {
                    saveBeforeFinish()
                }
            }

            imgTimeSettings.setOnClickListener {
                val intent = Intent(this@LimitationAppActivity, SettingTimeActivity::class.java)
                startActivity(intent.apply {
                    putExtra("childInfo", Gson().toJson(child))
                })
            }

            imgSetPin.setOnClickListener {
                openPinActivity("edit")
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
                        "Ошибка при сохранении, попробуйте еще раз",
                        Toast.LENGTH_SHORT
                    ).show()
                    goTo()
                }
                is ResultStatus.Success -> {
                    binding.pbLimitation.visible(false)
                    goTo()
                }
            }
        }
    }

    private fun openPinActivity(tag: String) {
        val intent = Intent(this, LockScreenActivity::class.java).apply {
            putExtra("tag", tag)
        }
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.e("pin", "child: ${AccessibilityPrefs.childInfo}")
                val confirm = result.data?.getStringExtra("confirm")
                val edit = result.data?.getStringExtra("edit")
                if (!confirm.isNullOrEmpty()) {
                    if (confirm.toBoolean())
                        vm.setLimits(AccessibilityPrefs.limitedApps)
                    else {
                        Toast.makeText(
                            this,
                            "Подтвердите пин, чтобы сохранить изменения!",
                            Toast.LENGTH_LONG
                        ).show()
                        adapter.notifyDataSetChanged()
                    }
                }
                if (!edit.isNullOrEmpty()) {
                    if (!edit.toBoolean()) {
                        Toast.makeText(this, "Установите пин!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    private fun setData(userApps: ArrayList<UserApp>) {
        userApps.forEachIndexed { index, app ->
            userApps[index].drawable =
                ApkInfoExtractor(this).getAppIconByPackageName(app.packageName)
        }
        adapter.updateList(userApps)
    }

    private fun saveBeforeFinish() {
        val appsLimit = adapter.getBlockedApps().ifEmpty { AccessibilityPrefs.limitedApps }
        if (appsLimit.isNotEmpty()) {
            AccessibilityPrefs.limitedApps = appsLimit
            openPinActivity("confirm")
        } else {
            goTo()
        }
    }

    override fun onBackPressed() {
        goTo()
        super.onBackPressed()
    }

    private fun goTo() {
        AccessibilityPrefs.limitedApps = HashMap()
        finish()
    }
}