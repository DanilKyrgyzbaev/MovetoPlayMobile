package com.movetoplay.presentation.app_nav

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.movetoplay.presentation.child_main_nav.ChildMainNav
import com.movetoplay.presentation.starting_nav.StartingNav
import com.movetoplay.presentation.ui.splash_screen.SplashScreen
import com.movetoplay.presentation.vm.app_vm.AppVM
import com.movetoplay.domain.manager.StateUserApp

@Composable
fun AppNav(
    viewModel : AppVM = hiltViewModel()
) {
    val nav = rememberNavController()
    val stateUserApp by viewModel.stateUserApp.collectAsState(null)
    LaunchedEffect(stateUserApp){
        when(stateUserApp){
            StateUserApp.NotAuthorized -> nav.navigate(AppRoute.Starting.route)
            StateUserApp.Parent -> nav.navigate(AppRoute.ParentContent.route)
            StateUserApp.Definition -> {}
            is StateUserApp.Children -> nav.navigate(AppRoute.ChildrenContent.route)
            null -> {}
        }
    }
    with(LocalContext.current as ComponentActivity){
        BackHandler(onBack = ::finish)
    }
    NavHost(navController = nav, startDestination = AppRoute.Splash.route){
        composable(AppRoute.Splash.route){
            SplashScreen()
        }
        composable(AppRoute.Starting.route){
            StartingNav()
        }
        composable(AppRoute.ChildrenContent.route){
            val selected = stateUserApp as StateUserApp.Children
            ChildMainNav(selected.selectedProfileChild)
        }
        composable(AppRoute.ParentContent.route){

        }
    }
}