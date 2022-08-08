package com.movetoplay.presentation.starting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.movetoplay.presentation.ui.first_time_widget.FirstTimeWidget
import com.movetoplay.presentation.ui.sign_in_widget.SignInWidget
import com.movetoplay.presentation.ui.sign_up_widget.SignUpWidget
import com.movetoplay.presentation.ui.splash_widget.SplashWidget
import com.movetoplay.presentation.ui.splash_widget.StateSplashWidget
import kotlinx.coroutines.delay


val s = mutableStateOf(StateSplashWidget.Full)

@Composable
fun StartingNav() {
    val nav = rememberNavController()

    LaunchedEffect(true ){
        delay(2000)
        s.value = StateSplashWidget.Half
    }
    SplashWidget(s){
        NavHost(navController = nav, startDestination = StartRoute.FirstTime.route){
            composable(StartRoute.FirstTime.route){
                FirstTimeWidget(
                    signInViaGoogle = {},
                    signUp = {nav.navigate(StartRoute.SignUp.route)},
                    signIn = {nav.navigate(StartRoute.SignIn.route)}
                )
            }
            composable(StartRoute.SignUp.route){
                SignUpWidget()
            }
            composable(StartRoute.SignIn.route){
                SignInWidget(
                    retrievePassword = {nav.navigate(StartRoute.RetrievePassword.route)}
                )
            }
            composable(StartRoute.RetrievePassword.route){

            }
        }
    }
}