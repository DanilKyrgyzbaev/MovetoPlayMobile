package com.movetoplay.presentation.starting_nav

sealed class StartRoute(val route: String){
    object FirstTime : StartRoute("first_time_route")
    object SignIn : StartRoute("sign_in_route")
    object SignUp : StartRoute("sign_up_route")
    object RestorePassword : StartRoute("restore_password_route")
}
