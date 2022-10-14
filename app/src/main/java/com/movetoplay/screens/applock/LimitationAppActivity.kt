package com.movetoplay.screens.applock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R
import com.movetoplay.data.model.user_apps.AppBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.screens.SettingTimeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LimitationAppActivity : AppCompatActivity() {

    private lateinit var adapter: LimitationsAppsAdapter
    private lateinit var btnFinish: Button
    private lateinit var imgDailyLimit: ImageView
    private lateinit var imgSetPassword: ImageView

    private val vm:LimitationAppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limitation_app)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        btnFinish.setOnClickListener {
            setLimitedAppsPrefs()
            finish()
        }

        imgDailyLimit.setOnClickListener {
            val intent = Intent(this, SettingTimeActivity::class.java)
            startActivity(intent)
        }

        imgSetPassword.setOnClickListener {
            val intent = Intent(this, LockScreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setLimitedAppsPrefs() {
        AccessibilityPrefs.limitedApps = adapter.getBlackListApps()
    }

    private fun getLimitedAppsPrefs(): HashSet<String> {
        var blockedAppsList: HashSet<String> = AccessibilityPrefs.limitedApps
        Log.e("adapter", "getSharedPrefs: $blockedAppsList")

        return blockedAppsList
    }

    private fun initViews() {
        imgDailyLimit = findViewById(R.id.img_time_settings)
        imgSetPassword = findViewById(R.id.img_set_pin)

        adapter = LimitationsAppsAdapter(
            this,
            ApkInfoExtractor(this).GetAllInstalledApkInfo(), getLimitedAppsPrefs()
        )
        btnFinish = findViewById(R.id.btn_finish)

        findViewById<RecyclerView>(R.id.rv_limitations).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@LimitationAppActivity.adapter
        }

        if (!isAccessibilityGranted(this)) {
            permissionAccessibility(this)
        } else {
            Log.d("ololo", "ooooooooo")
        }

        vm.sendLimitedApps(getApps())
    }
    private fun getApps(): UserAppsBody {
        val apps = ArrayList<AppBody>()
        val extractor = ApkInfoExtractor(this)

        extractor.GetAllInstalledApkInfo().forEach {
            apps.add(AppBody(extractor.GetAppName(it), it))
        }
        return UserAppsBody(apps)
    }

    private fun isAccessibilityGranted(context: Context): Boolean {

        var accessibilityEnabled = 0
        val service = context.packageName + "/" + AccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun permissionAccessibility(context: Context) {
        AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle("")
            .setView(
                LayoutInflater.from(context).inflate(
                    R.layout.view_dialog_permission_accessibility,
                    null,
                    false
                )
            )
            .setPositiveButton("Настройки") { _, _ ->
                //Utils.reportEventClick("AppLock Screen", "AppLock_Permission_btn")
                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .create().show()
    }
}
