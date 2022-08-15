package com.movetoplay.presentation.ui.child_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.domain.model.Exercise
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.ExercisesPerformedIndicator
import com.movetoplay.presentation.ui.component_widgets.TypeButton

@Composable
fun ExercisesPerformedOnDay(
    sizeButtonAndIndicators: DpSize,
    listExercise: List<Exercise>,
    performExercise:()->Unit
) {
    val list = remember {
        listExercise
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.exercises_performed_on_day),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600
        )
        Spacer(modifier = Modifier.height(26.dp))
        for (exercise in list){
            ExercisesPerformedIndicator(
                exercise = exercise,
                size = sizeButtonAndIndicators
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Button(
            label = stringResource(R.string.perform_exercise) ,
            onClick = performExercise,
            size = sizeButtonAndIndicators,
            typeButton = TypeButton.Outline
        )
    }
}