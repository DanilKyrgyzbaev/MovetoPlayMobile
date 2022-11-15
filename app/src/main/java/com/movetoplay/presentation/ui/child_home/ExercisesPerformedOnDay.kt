package com.movetoplay.presentation.ui.child_home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.db.UserEntity
import com.movetoplay.domain.model.Exercise
import com.movetoplay.pref.Pref
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.ExercisesPerformedIndicator
import com.movetoplay.presentation.ui.component_widgets.TypeButton

@Composable
fun ExercisesPerformedOnDay(
    sizeButtonAndIndicators: DpSize,
    listExercise: List<Exercise>,
    defExerciseCount: HashMap<String,Int>,
    performExercise: () -> Unit
) {
    val list = remember {
        listExercise
    }
    val userEntity: UserEntity? = null
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
        Log.e("Pref", Pref.userLogin)
        Spacer(modifier = Modifier.height(26.dp))
        for (exercise in list) {
            ExercisesPerformedIndicator(
                exercise = exercise,
                size = sizeButtonAndIndicators
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier
                .width(300.dp)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
        ) {
            val jumps = Pref.jumps
            val defJumps = defExerciseCount["jump"]
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(40.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF1790D4),
                                    Color(0xFF1790D4)
                                )
                            )
                        ).width(300.dp * jumps / defJumps!!)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Прыжки",
                        Modifier.padding(7.dp),
                        color = Color.White,
                        textAlign = TextAlign.Left,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "$jumps/$defJumps",
                        modifier = Modifier.padding(7.dp),
                        color = Color.White,
                        textAlign = TextAlign.Right,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .width(300.dp)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
        ) {
            val squeezing = Pref.push_ups
            val defSqueezing = defExerciseCount["squeezing"]
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(40.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF1790D4),
                                    Color(0xFF1790D4)
                                )
                            )
                        )
                        .width(300.dp * squeezing / defSqueezing!!)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Отжимания",
                        Modifier.padding(7.dp),
                        color = Color.White,
                        textAlign = TextAlign.Left,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "$squeezing/$defSqueezing",
                        modifier = Modifier.padding(7.dp),
                        color = Color.White,
                        textAlign = TextAlign.Right,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .width(300.dp)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
        ) {
            val squats = Pref.sits
            val defSquats = defExerciseCount["squat"]
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(40.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF1790D4),
                                    Color(0xFF1790D4)
                                )
                            )
                        )
                        .width(300.dp * squats / defSquats!!)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Приседания",
                        Modifier.padding(7.dp),
                        color = Color.White,
                        textAlign = TextAlign.Left,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "$squats/$defSquats",
                        modifier = Modifier.padding(7.dp),
                        color = Color.White,
                        textAlign = TextAlign.Right,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            label = stringResource(R.string.perform_exercise),
            onClick = performExercise,
            size = sizeButtonAndIndicators,
            typeButton = TypeButton.Outline
        )
    }
}
