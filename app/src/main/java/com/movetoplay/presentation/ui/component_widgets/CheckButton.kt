package com.movetoplay.presentation.ui.component_widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CheckButton(
    value : Boolean,
    onValueChange: (Boolean) -> Unit,
    size: DpSize,
    shape : RoundedCornerShape = RoundedCornerShape(100)
) {
    val color = colorScheme.primary
    Box(
        modifier = Modifier.size(size)
            .border(1.5.dp, color, shape)
            .clip(shape)
            .clickable { onValueChange(!value) }
            .padding(6.dp),
    ){
        AnimatedVisibility(
            visible = value,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier= Modifier.fillMaxSize(),
                tint = color
            )
        }

    }

}