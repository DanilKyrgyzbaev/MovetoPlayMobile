package com.movetoplay.screens.applock

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R
import java.io.IOException


class LimitationAppActivity : AppCompatActivity() {

    private lateinit var adapter: LimitationsAppsAdapter
    private lateinit var btnFinish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limitation_app)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        btnFinish.setOnClickListener{
            sharedPrefs()
            finish()
        }
    }

    private fun sharedPrefs() {
        adapter.notifyDataSetChanged()
        val prefs: SharedPreferences =
            getSharedPreferences("SHARED_PREFS_FILE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        try {
            editor.putStringSet("LimitApps", adapter.getBlackListApps())
            Log.e("LimitApps", "SUCCESS: " + adapter.getBlackListApps())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        adapter.notifyDataSetChanged()
        editor.apply()
    }

    private fun initViews() {

        adapter = LimitationsAppsAdapter(
            this,
            ApkInfoExtractor(this).GetAllInstalledApkInfo()
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
    }

    fun isAccessibilityGranted(context: Context): Boolean {

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
            .setPositiveButton("Настройки") { dialog, which ->
                //Utils.reportEventClick("AppLock Screen", "AppLock_Permission_btn")
                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .create().show()
    }
}
