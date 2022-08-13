package com.movetoplay.presentation.child_main_nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.movetoplay.presentation.ui.camera_for_exercise.CameraForExercise

@Composable
fun ChildMainNav() {
    val nav = rememberNavController()
    NavHost(navController = nav,
        startDestination = MainRoute.ContentWithBottomNavBar.route){
        composable(MainRoute.ContentWithBottomNavBar.route){
            ContentNav(){
                nav.navigate(MainRoute.CameraExercisePerformance.route)
            }
        }
        composable(MainRoute.CameraExercisePerformance.route){
            CameraForExercise()
        }
    }
}