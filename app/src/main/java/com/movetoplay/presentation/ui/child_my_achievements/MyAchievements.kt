package com.movetoplay.presentation.ui.child_my_achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.presentation.vm.profile_childe_vm.ProfileChildVM

@Composable
fun MyAchievements(
    viewModel: ProfileChildVM) {
    val sizeButtonAndIndicators = DpSize(300.dp, 40.dp)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(46.dp))
        Text(
            text = stringResource(R.string.my_achievements),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600
        )
        Spacer(modifier = Modifier.height(20.dp))
        ExercisesPerformedInTotal(
            sizeButtonAndIndicators,
            listExercise = viewModel.listExerciseFRemainingTime
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Награды",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(22.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(151.dp)
                ){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(R.drawable.ic_medal_outline),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Медалей",
                            color = Color(0xFF888888),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                    .width(80.dp)
                    .height(151.dp)){

                    Column(
                        modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(R.drawable.ic_license),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Орденов",
                            color = Color(0xFF888888),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }
        }
    }
}