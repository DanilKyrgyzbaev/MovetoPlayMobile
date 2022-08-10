package com.movetoplay.presentation.children_main_nav

sealed class MainRoute(val route:String){
    object ContentWithBottomNavBar : MainRoute("content_with_bottom_nav_bar_route")
    object CameraExercisePerformance : MainRoute("camera_exercise_performance_route")
}
