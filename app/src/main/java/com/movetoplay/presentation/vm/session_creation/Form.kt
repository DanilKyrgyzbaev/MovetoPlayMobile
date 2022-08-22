package com.movetoplay.presentation.vm.session_creation

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import com.movetoplay.domain.model.Role

class Form(
    email:String = "",
    password:String = "",
    age:Byte? = null,
    role:Role = Role.Parent
){
    var email by mutableStateOf(email)
    var password by mutableStateOf(password)
    var age by mutableStateOf(age)
    var role by mutableStateOf(role)
}