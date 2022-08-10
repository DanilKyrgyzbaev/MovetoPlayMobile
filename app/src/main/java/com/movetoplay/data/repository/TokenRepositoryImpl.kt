package com.movetoplay.data.repository

import android.content.SharedPreferences
import com.movetoplay.data.utils.stringNullable


class TokenRepositoryImpl(sp: SharedPreferences) {
    var token by sp.stringNullable()
}