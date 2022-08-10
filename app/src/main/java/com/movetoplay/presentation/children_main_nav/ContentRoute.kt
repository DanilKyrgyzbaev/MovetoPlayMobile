package com.movetoplay.presentation.children_main_nav

import androidx.annotation.DrawableRes
import com.movetoplay.R

sealed class ContentRoute(val route:String, @DrawableRes val iconRes: Int) {
    object Home : ContentRoute("main_route", R.drawable.ic_home)
    object MyAccomplishments : ContentRoute("my_accomplishments_route",R.drawable.ic_cup)
}