package com.movetoplay.presentation.ui.child_home

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.utils.timeFormat
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun TimeUse(
    availableForDayMinutes: Int,
    remainderMinutes: Int,
    addTime: () -> Unit,
    sizeButton: DpSize
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(colorScheme.surface, RoundedCornerShape(10.dp))
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${stringResource(R.string.available_for_day)}: $remainderMinutes мин",
            color = Color.Gray,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.remainder),
            color = colorScheme.primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.W600
        )
        Text(
            text = "$availableForDayMinutes мин",
            color = colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.W600
        )
        Spacer(modifier = Modifier.height(26.dp))
        Button(
            label = "+ 10 минут",
            onClick = {
                val remainingTime = AccessibilityPrefs.remainingTime.toInt()
                val dailyLimit = AccessibilityPrefs.dailyLimit.toInt()
                val addTime = 10000
                val resultRemainingTime = remainingTime + addTime
                val resultDailyLimit = dailyLimit + addTime
                AccessibilityPrefs.remainingTime = resultRemainingTime.toLong()
                AccessibilityPrefs.dailyLimit = resultDailyLimit.toLong()
            },
            size = sizeButton
        )
    }
}
