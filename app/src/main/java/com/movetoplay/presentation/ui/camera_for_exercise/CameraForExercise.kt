package com.movetoplay.presentation.ui.camera_for_exercise

import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skgmn.cameraxx.CameraPreview
import com.movetoplay.presentation.ui.component_widgets.LoadingContent
import com.movetoplay.presentation.vm.execution_of_exercise.ExecutionOfExerciseVM

@Composable
fun CameraForExercise(
    viewModel: ExecutionOfExerciseVM
) {
    val isInit by remember {
        viewModel.isInit
    }
    if (isInit){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBarProgress(
                stateExercise = viewModel.exercise
            )
            CameraPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
                imageAnalysis = viewModel.analysisImage
            )
            ToolsBottomBar(
                stop = {}
            )
        }
    }else{
        LoadingContent()
    }
}
