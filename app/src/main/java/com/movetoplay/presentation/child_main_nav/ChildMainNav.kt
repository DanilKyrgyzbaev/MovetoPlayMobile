package com.movetoplay.presentation.child_main_nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.TypeExercise
import com.movetoplay.presentation.ui.camera_for_exercise.CameraForExercise
import com.movetoplay.presentation.ui.select_profile_child.SelectProfileChild
import com.movetoplay.presentation.vm.execution_of_exercise.EventExecutionOfExercise
import com.movetoplay.presentation.vm.execution_of_exercise.ExecutionOfExerciseVM
import com.movetoplay.presentation.vm.profile_childe_vm.ProfileChildVM

@Composable
fun ChildMainNav(
    selectedProfileChild: Boolean
) {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = if (selectedProfileChild) {
            MainRoute.ContentWithBottomNavBar.route
        } else MainRoute.SelectProfileChild.route
    ) {
        composable(MainRoute.ContentWithBottomNavBar.route) {
            ContentNav {
                nav.navigate(MainRoute.CameraExercisePerformance.allRoute(it.name))
            }
        }
        composable(MainRoute.CameraExercisePerformance.allRoute()) { backStackEntry ->
            val type = backStackEntry.arguments?.getString(MainRoute.CameraExercisePerformance.arg_type)!!
            val viewModel: ExecutionOfExerciseVM = hiltViewModel()
            LaunchedEffect(type) {
                viewModel.onEvent(
                    EventExecutionOfExercise.InitTypeExercise(
                        TypeExercise.valueOf(type)
                    )
                )
            }
            CameraForExercise(viewModel)
        }
        composable(MainRoute.SelectProfileChild.route) {
            SelectProfileChild(
                showContent = {
                    nav.navigate(MainRoute.ContentWithBottomNavBar.route) {
                        popUpTo(MainRoute.SelectProfileChild.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
