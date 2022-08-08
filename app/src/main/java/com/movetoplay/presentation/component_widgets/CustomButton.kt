package com.movetoplay.presentation.component_widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.movetoplay.R

@Composable
fun CustomLoadingButton(
    state: Boolean,
    onChange: (Boolean)->Unit,
    label: String,
    loadingLabel: String = stringResource(R.string.loading),
    icon: Painter? = null,
) {
    ElevatedButton(
        onClick = {onChange(!state)},
        modifier =  Modifier.animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        icon?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = if(state) loadingLabel else label
        )
        if (state){
            Spacer(modifier = Modifier.width(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = colorScheme.primary
            )
        }
    }
}