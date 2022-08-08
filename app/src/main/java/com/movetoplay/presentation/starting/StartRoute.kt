package com.movetoplay.presentation.starting

sealed class StartRoute(val route: String){
    object FirstTime : StartRoute("first_time_route")
    object SignIn : StartRoute("sign_in_route")
    object SignUp : StartRoute("sign_up_route")
    object RetrievePassword : StartRoute("retrieve_password_route")
}
