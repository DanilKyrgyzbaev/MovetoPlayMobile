package com.movetoplay.presentation.ui.splash_widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun SplashWidget(
    state: State<StateSplashWidget>,
    background: @Composable BoxScope.()->Unit
) {
    val stateSplashWidget by remember { state }
    val fraction by animateFloatAsState(targetValue = stateSplashWidget.scale)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(fraction = 1-fraction),
            content = background
        )
        LogoWidget(stateSplashWidget)
    }
}

