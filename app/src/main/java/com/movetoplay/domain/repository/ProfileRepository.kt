package com.movetoplay.domain.repository

import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.Role
import com.movetoplay.domain.model.TokenResponse

interface ProfileRepository {
    var token: TokenResponse?
    var role: Role?
    var child : Child?
    var email: String?
    var password: String?
    fun clear()
}