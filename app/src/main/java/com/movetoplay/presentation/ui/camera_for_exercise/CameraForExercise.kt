package com.movetoplay.presentation.ui.camera_for_exercise

import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skgmn.cameraxx.CameraPreview
import com.movetoplay.presentation.vm.execution_of_exercise.ExecutionOfExerciseVM

@Composable
fun CameraForExercise(
    viewModel: ExecutionOfExerciseVM = hiltViewModel()
) {

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
}
