package com.movetoplay.presentation.utils

fun timeFormat(min: Int): String{
    val hours = min/60
    val minutes = min%60
    val hoursText = when(hours){
        0-> ""
        1->"$hours час "
        in 2..4 -> "$hours часа "
        in 5..20->"$hours часов "
        else ->{
            when(hours%10){
                1->"$hours час "
                in 2..4 -> "$hours часа "
                in 5..9->"$hours часов "
                else->"$hours часов "
            }
        }
    }
    val minutesText = when(minutes){
        0-> ""
        1->"$minutes минута"
        in 2..4 -> "$minutes минуты"
        in 5..20->"$minutes минут"
        else ->{
            when(minutes%10){
                1->"$minutes минута"
                in 2..4 -> "$minutes минуты"
                in 5..9->"$minutes минут"
                else->"$minutes минут"
            }
        }
    }
    return "$hoursText$minutesText"
}