package com.movetoplay.presentation.ui.child_home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.movetoplay.domain.model.Exercise

@Composable
fun Home(
    openCameraForExercise: ()->Unit
) {
    val sizeButtonAndIndicators= DpSize(300.dp, 40.dp)
    val stateScroll = rememberScrollState()
    var visibleDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(stateScroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(46.dp))
        TimeUse(
            availableForDayMinutes = 123,
            remainderMinutes = 90,
            addTime = {},
            sizeButton = sizeButtonAndIndicators
        )
        Spacer(modifier = Modifier.height(20.dp))
        ExercisesPerformedOnDay(
            sizeButtonAndIndicators,
            listExercise = listOf(
                Exercise("Прыжки", 0,30),
                Exercise("Отжимания", 0,30),
                Exercise("Приседания", 0,30)
            ),
            performExercise = {visibleDialog = true}
        )
    }
    if(visibleDialog){
        SelectExercise(
            onDismiss = {visibleDialog = false},
            chose = {
                openCameraForExercise()
                visibleDialog = false
            },
            sizeButtonAndIndicators,
            listExercise = listOf("Прыжки", "Отжимания", "Приседания")
        )
    }
}