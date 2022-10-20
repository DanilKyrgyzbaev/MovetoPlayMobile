package com.movetoplay.util

import android.view.View

fun View.visible(isVisible:Boolean){
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.inVisible(inVisibility: Boolean) {
    this.visibility = if (inVisibility) View.INVISIBLE
    else View.VISIBLE
}