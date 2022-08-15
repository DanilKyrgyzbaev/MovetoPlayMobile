package com.movetoplay.presentation.ui.component_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.domain.model.Exercise
import com.movetoplay.domain.model.TypeExercise

@Composable
fun ExercisesPerformedIndicator(
    exercise : Exercise,
    size: DpSize,
    showMax: Boolean = true,
    shape : RoundedCornerShape = RoundedCornerShape(10.dp)
) {
    Row(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = when(exercise.type){
                TypeExercise.StarJump -> stringResource(R.string.jumps)
                TypeExercise.Squats -> stringResource(R.string.squats)
                TypeExercise.Pushups -> stringResource(R.string.pushups)
            },
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.W600
        )
        Text(
            text = if(showMax)
                "${exercise.count}/${exercise.max}"
            else
                exercise.count.toString(),
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.W600
        )
    }
}