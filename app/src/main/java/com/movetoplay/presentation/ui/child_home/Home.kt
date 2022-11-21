package com.movetoplay.presentation.ui.child_home

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.movetoplay.computer_vision.ComputerVisionActivity
import com.movetoplay.domain.model.TypeExercise
import com.movetoplay.presentation.vm.profile_childe_vm.ProfileChildVM

@Composable
fun Home(
    viewModel: ProfileChildVM,
    openCameraForExercise: (TypeExercise) -> Unit
) {
    val sizeButtonAndIndicators = DpSize(300.dp, 40.dp)
    viewModel.getDailyExercises()
    val stateScroll = rememberScrollState()
    var visibleDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(stateScroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(46.dp))
        TimeUse(
            availableForDayMinutes = viewModel.availableForDay.value.toLong(),
            remainderMinutes = viewModel.flowRemainingTime.collectAsState(initial = 60 * 60000).value.toLong(),
            addTime = {
                val addTimePl = 10000
            },
            sizeButton = sizeButtonAndIndicators
        )
        Spacer(modifier = Modifier.height(20.dp))
        ExercisesPerformedOnDay(
            sizeButtonAndIndicators,
            listExercise = viewModel.listExerciseForDay,
            defExerciseCount = viewModel.defExerciseCount.value,
            chose = {
                context.startActivity(Intent(context, ComputerVisionActivity::class.java))
                visibleDialog = false
            },
            sizeButton = sizeButtonAndIndicators,
            dailyExercises = viewModel.dailyExercises.value
        ) {
            visibleDialog = true
        }
    }
    if (visibleDialog) {
        SelectExercise(
            onDismiss = { visibleDialog = false },
            chose = {
//                openCameraForExercise(it)
                context.startActivity(Intent(context, ComputerVisionActivity::class.java))
                visibleDialog = false
            },
            sizeButtonAndIndicators,
            listExercise = TypeExercise.values().toList()
        )
    }
}
