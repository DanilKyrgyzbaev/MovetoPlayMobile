package com.movetoplay.util

import java.util.concurrent.TimeUnit

fun Long.toStrTime(): String {
    val m: Long = this / (1000 * 60) % 60
    val h: Long = this /(1000 * 60 * 60) % 24

//    val s = TimeUnit.MILLISECONDS.toSeconds(this)
//    val m = TimeUnit.MILLISECONDS.toMinutes(this)
//    val h = TimeUnit.MILLISECONDS.toHours(this)
    var str = if (h > 0) "${h}ч " else ""
    str += if (m > 0) "${m}м " else ""
    return str
}