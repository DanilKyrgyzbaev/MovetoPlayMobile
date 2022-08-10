package com.movetoplay.presentation.starting_nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.movetoplay.presentation.ui.first_time.FirstTime
import com.movetoplay.presentation.ui.restore_password.RestorePassword
import com.movetoplay.presentation.ui.sign_in.SignIn
import com.movetoplay.presentation.ui.sign_up.SignUp


@Composable
fun StartingNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = StartRoute.FirstTime.route){
        composable(StartRoute.FirstTime.route){
            FirstTime(
                signUp = {nav.navigate(StartRoute.SignUp.route)},
                signIn = {nav.navigate(StartRoute.SignIn.route)}
            )
        }
        composable(StartRoute.SignUp.route){
            SignUp()
        }
        composable(StartRoute.SignIn.route){
            SignIn(
                retrievePassword = {nav.navigate(StartRoute.RestorePassword.route)}
            )
        }
        composable(StartRoute.RestorePassword.route){
            RestorePassword()
        }
    }
}