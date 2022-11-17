package com.movetoplay.screens

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.movetoplay.presentation.app_nav.AppNav
import com.movetoplay.presentation.child_main_nav.ChildMainNav
import com.movetoplay.presentation.theme.MoveToPlayTheme
import com.movetoplay.screens.applock.AccessibilityService
import com.movetoplay.util.permissionAccessibilityAlert
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Method


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var cont: Context
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isMiUi()
        checkAccessPermission()
        checkPermission()
        initViews()
    }

    private fun isMiUi() {
        try {
            val cl = this.classLoader
            val c = cl.loadClass("android.os.SystemProperties")
            val get: Method = c.getMethod("get", String::class.java)
            val miui = get.invoke(c, "ro.miui.ui.version.name") as String
            if (miui.isNotEmpty() && miui.contains("11.0.3.0")) {
                val intent = Intent()
                intent.apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.parse(("package:$packageName"))
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                }
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.e("main", "isMiUi: ${e.localizedMessage}")
        }
    }

    private fun initViews() {
        cont = this
        vm.syncApps(this)
        setContent {
            MoveToPlayTheme(false) {
                AppNav()
                ChildMainNav(selectedProfileChild = true)
            }
        }
    }

    private fun checkPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
    }

    override fun onRestart() {
        super.onRestart()
        checkAccessPermission()
        checkPermission()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private fun checkAccessPermission() {
        if (!isAccessibilityGranted(this)) {
            permissionAccessibility()
        }
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

    private fun permissionAccessibility() {
        this.permissionAccessibilityAlert()
    }
}
