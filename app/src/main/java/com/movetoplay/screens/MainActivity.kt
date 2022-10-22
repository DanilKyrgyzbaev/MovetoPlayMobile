package com.movetoplay.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.movetoplay.R
import com.movetoplay.presentation.app_nav.AppNav
import com.movetoplay.presentation.child_main_nav.ChildMainNav
import com.movetoplay.presentation.theme.MoveToPlayTheme
import com.movetoplay.screens.applock.AccessibilityServiceVersion2
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var cont: Context
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAccessPermission()
        checkPermission()
        initViews()
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
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
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
            permissionAccessibility(this)
        }
    }
    private fun isAccessibilityGranted(context: Context): Boolean {

        var accessibilityEnabled = 0
        val service = context.packageName + "/" + AccessibilityServiceVersion2::class.java.canonicalName
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
