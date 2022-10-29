package com.movetoplay.data.repository

import android.content.SharedPreferences
import com.movetoplay.data.utils.stringNullable
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.Role
import com.movetoplay.domain.model.TokenResponse
import com.movetoplay.domain.repository.ProfileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ProfileRepositoryImpl(sp: SharedPreferences) : ProfileRepository {
    private var stringToken by sp.stringNullable()
    private var stringRole by sp.stringNullable()
    override var role: Role?
        get() = stringRole?.let { Role.valueOf(it) }
        set(value) {stringRole = value?.name}
    private var stringChild by sp.stringNullable()
    override var child: Child?
        get() = stringChild?.let { Json.decodeFromString(it) }
        set(value) { stringChild =  Json.encodeToString(value) }
    override var token: TokenResponse?
        get() = stringToken?.let { Json.decodeFromString(it) }
        set(value) { stringToken =  Json.encodeToString(value) }

    override var email: String? by sp.stringNullable()
    override var password: String? by sp.stringNullable()
    override fun clear() {
        token = null
        role = null
        child = null
        email = null
        password = null
    }

}