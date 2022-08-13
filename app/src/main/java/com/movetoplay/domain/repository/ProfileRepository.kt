package com.movetoplay.domain.repository

import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.Role

interface ProfileRepository {
    var token: String?
    var role: Role?
    var child : Child?
    var email: String?
    var password: String?
}