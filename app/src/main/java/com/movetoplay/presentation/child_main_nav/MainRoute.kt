package com.movetoplay.presentation.child_main_nav

sealed class MainRoute(val route:String){
    object ContentWithBottomNavBar : MainRoute("content_with_bottom_nav_bar_route")
    object CameraExercisePerformance : MainRoute("camera_exercise_performance_route"){
        val arg_type = "JumpType"
        fun allRoute(type: String="{$arg_type}") = "$route/$type"
    }
    object SelectProfileChild : MainRoute("select_profile_child_route")
}
