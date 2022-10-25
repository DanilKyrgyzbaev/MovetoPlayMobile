package com.movetoplay.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView

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