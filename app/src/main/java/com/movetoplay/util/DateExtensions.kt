package com.movetoplay.util

fun Long.toStrTime(): String {
    val s: Long =  (this / 1000) % 60
    val m: Long = this / (1000 * 60) % 60
    val h: Long = this /(1000 * 60 * 60) % 24

   return  if (h > 0) "${h}ч " else "" + if (m > 0) "${m}м " else "" + if (s > 0) "${s}с" else ""
}