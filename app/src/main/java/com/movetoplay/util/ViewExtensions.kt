package com.movetoplay.util

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.movetoplay.R

fun View.visible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.inVisible(inVisibility: Boolean) {
    this.visibility = if (inVisibility) View.INVISIBLE
    else View.VISIBLE
}

fun ImageView.load(drawable: Drawable) {
    this.setImageDrawable(drawable)
}

fun Context.permissionAccessibilityAlert(){
    AlertDialog.Builder(this, R.style.AlertDialogTheme)
        .setTitle("")
        .setView(
            LayoutInflater.from(this).inflate(
                R.layout.view_dialog_permission_accessibility,
                null,
                false
            )
        )
        .setPositiveButton("Настройки") { _, _ ->
            // Utils.reportEventClick("AppLock Screen", "AppLock_Permission_btn")
            this.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        .create().show()
}