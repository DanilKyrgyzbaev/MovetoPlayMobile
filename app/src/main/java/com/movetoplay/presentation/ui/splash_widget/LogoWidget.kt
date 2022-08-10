package com.movetoplay.presentation.ui.splash_widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R

@Composable
fun LogoWidget(
    state: StateSplashWidget
) {
    val fraction by animateFloatAsState(targetValue = state.scale)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxHeight(fraction = fraction)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        val height = this.maxHeight
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleX = 1.5f, scaleY = 1f)
                .background(
                    colorScheme.surface,
                    if(state == StateSplashWidget.Full) RoundedCornerShape(0)
                    else RoundedCornerShape(bottomEndPercent = 100, bottomStartPercent = 100)
                )

        )
        if (state != StateSplashWidget.Unoccupied){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(height/4)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "MOVETOPLAY",
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = (height /18).value.sp,
                )
            }
        }
    }
}