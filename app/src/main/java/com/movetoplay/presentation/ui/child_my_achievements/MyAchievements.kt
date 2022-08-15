package com.movetoplay.presentation.ui.child_my_achievements

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.domain.model.Exercise
import com.movetoplay.presentation.vm.profile_childe_vm.ProfileChildVM

@Composable
fun MyAchievements(
    viewModel: ProfileChildVM
) {
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
    }
}