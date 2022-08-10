package com.movetoplay.presentation.children_main_nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNav() {
    val nav = rememberNavController()
    NavHost(navController = nav,
        startDestination = MainRoute.ContentWithBottomNavBar.route){
        composable(MainRoute.ContentWithBottomNavBar.route){

        }
        composable(MainRoute.CameraExercisePerformance.route){

        }
    }
}