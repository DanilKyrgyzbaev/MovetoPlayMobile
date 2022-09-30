package com.movetoplay.presentation.ui.camera_for_exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.domain.model.Exercise
import com.movetoplay.domain.model.TypeExercise
@Preview
@Composable
fun TopBarProgress(
    stateExercise: State<Exercise>
) {
    val exercise by remember {
        stateExercise
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = when(exercise.type){
                TypeExercise.StarJump -> stringResource(R.string.jumps)
                TypeExercise.Squats -> stringResource(R.string.squats)
                TypeExercise.Pushups -> stringResource(R.string.pushups)
            },
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600
        )
        Text(
            text = exercise.count.toString(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600
        )
    }
}