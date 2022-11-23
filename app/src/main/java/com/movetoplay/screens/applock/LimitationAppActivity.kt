package com.movetoplay.screens.applock

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.screens.set_time.SettingTimeActivity
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint
import com.movetoplay.databinding.ActivityLimitationAppBinding
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.pref.AccessibilityPrefs

@AndroidEntryPoint
class LimitationAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLimitationAppBinding
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
    }

    private fun onItemClick(app: UserApp) {
        if (vm.setLimitAppCount.value!! <= 3 && child.pinCode!=null) {
            vm.setLimit(app)
        }
        else if(child.pinCode == null) startActivity(Intent(this, LockScreenActivity::class.java).apply {
            putExtra("pin_type", "set")
        })
    }

    private fun initListeners() {
        binding.apply {
            btnFinish.setOnClickListener {
                goTo()
            }

            imgTimeSettings.setOnClickListener {
                val intent = Intent(this@LimitationAppActivity, SettingTimeActivity::class.java)
                startActivity(intent.apply {
                    putExtra("childInfo", Gson().toJson(child))
                })
            }

            imgSetPin.setOnClickListener {
                val intent = Intent(this@LimitationAppActivity, LockScreenActivity::class.java)
                startActivity(intent)
            }
        }
        vm.setLimitAppCount.observe(this) {
            if (it == 3) {
                startActivity(Intent(this, LockScreenActivity::class.java).apply {
                    putExtra("pin", child.pinCode)
                    putExtra("pin_type", "check")
                })
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
                    binding.viewFocus.visible(true)
                        //  Toast.makeText(this, "Данные сохраняются...", Toast.LENGTH_SHORT).show()
                }
                is ResultStatus.Error -> {
                    binding.pbLimitation.visible(false)
                    Toast.makeText(
                        this,
                        " Ошибка при сохранении, попробуйте еще раз",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.viewFocus.visible(false)
                   // goTo()
                }
                is ResultStatus.Success -> {
                    binding.pbLimitation.visible(false)
                    binding.viewFocus.visible(false)
                 //   Toast.makeText(this, "Данные успешно сохранились!", Toast.LENGTH_SHORT).show()
                  //  goTo()
                }
            }
        }

    }

    private fun setData(userApps: ArrayList<UserApp>) {
        userApps.forEachIndexed { index, app ->
            userApps[index].drawable =
                ApkInfoExtractor(this).getAppIconByPackageName(app.packageName)
        }
        binding.rvLimitations.adapter = LimitationsAppsAdapter(userApps, this::onItemClick)
    }

    private fun saveBeforeFinish() {
//        val appsLimit = adapter.getBlockedApps()
//        if (appsLimit.isNotEmpty())
//            vm.setLimits(appsLimit)
//        else goTo()
    }

    override fun onBackPressed() {
        goTo()
        super.onBackPressed()
    }

    private fun goTo() {
        finish()
    }
}