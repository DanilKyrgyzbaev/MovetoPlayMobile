package com.movetoplay.presentation.ui.camera_for_exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button
@Preview
@Composable
fun ToolsBottomBar(
    stop: ()-> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ){
        Button(label = stringResource(R.string.stop),
            onClick = stop,
            size = DpSize(300.dp,48.dp)
        )
    }
}