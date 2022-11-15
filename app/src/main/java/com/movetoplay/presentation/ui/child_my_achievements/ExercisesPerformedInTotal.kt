package com.movetoplay.presentation.ui.child_my_achievements // ktlint-disable package-name

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
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
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.model.Exercise
import com.movetoplay.pref.Pref
import com.movetoplay.presentation.ui.component_widgets.ExercisesPerformedIndicator

@Composable
fun ExercisesPerformedInTotal(
    sizeButtonAndIndicators: DpSize,
    defExerciseCount: HashMap<String, Int>,
    listExercise: List<Exercise>,
    dailyExercises: DailyExercises
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
            text = stringResource(R.string.exercises_performed),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600
        )
        Spacer(modifier = Modifier.height(5.dp))

        Column(
            modifier = Modifier
                .width(300.dp)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
        ) {
            val jumps = dailyExercises.jumps?.count ?: Pref.jumps
            val defJumpsCount = defExerciseCount["jump"]
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
                        .width(300.dp * jumps / defJumpsCount!!)
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
                        text = "$jumps/$defJumpsCount",
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
            val squeezing = dailyExercises.squeezing?.count ?: Pref.push_ups
            val defSqueezingCount = defExerciseCount["squeezing"]
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
                        .width(300.dp * squeezing / defSqueezingCount!!)
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
                        text = "$squeezing/$defSqueezingCount",
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
            val squats = dailyExercises.squats?.count ?: Pref.sits
            val defSquatCount = defExerciseCount["squat"]
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
                        .width(300.dp * squats / defSquatCount!!)
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
                        text = "$squats/$defSquatCount",
                        modifier = Modifier.padding(7.dp),
                        color = Color.White,
                        textAlign = TextAlign.Right,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))

        for (exercise in list) {
            ExercisesPerformedIndicator(
                exercise = exercise,
                size = sizeButtonAndIndicators
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
