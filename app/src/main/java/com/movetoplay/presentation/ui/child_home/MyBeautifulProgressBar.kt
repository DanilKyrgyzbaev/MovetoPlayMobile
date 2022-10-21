package com.movetoplay.presentation.ui.child_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyBeautifulProgressBar(
    text: String,
    progress: Int,
    max: Int,
    modifier: Modifier = Modifier,
    colorBackground: Color = Color.Blue,
    colorProgress: Color = Color.Cyan
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorBackground)

    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.toFloat() / max)
                .align(Alignment.CenterStart)
                .background(color = colorProgress)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(7.dp)
        )

        Text(
            text = "$progress/$max",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(7.dp)
        )
    }
}