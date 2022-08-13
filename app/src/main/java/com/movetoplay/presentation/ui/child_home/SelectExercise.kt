package com.movetoplay.presentation.ui.child_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button

@Composable
fun SelectExercise(
    onDismiss: () -> Unit,
    chose: (String) -> Unit,
    sizeButton: DpSize,
    listExercise: List<String>,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.select_type_exercise),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600
            )
            Spacer(modifier = Modifier.height(26.dp))
            for (exercise in listExercise){
                Button(
                    label = exercise,
                    onClick = {chose(exercise)},
                    size = sizeButton
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}