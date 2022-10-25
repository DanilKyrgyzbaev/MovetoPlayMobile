package com.movetoplay.util

import java.net.NetworkInterface
import java.util.*


fun getMacAddress(): String {
    try {
        val all: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (nif in all) {
            if (!nif.name.equals("wlan0", ignoreCase = true)) continue
            val macBytes: ByteArray = nif.hardwareAddress ?: return ""
            val res1 = StringBuilder()
            for (b in macBytes) {
                res1.append(String.format("%02X:", b))
            }
            if (res1.isNotEmpty()) {
                res1.deleteCharAt(res1.length - 1)
            } else return "02:00:00:00:00:00"

            return res1.toString()
        }
    } catch (ex: Exception) {
    }
    return "02:00:00:00:00:00"
}

fun getDeviceName(): String {
    return android.os.Build.BRAND + " " + android.os.Build.MODEL
}