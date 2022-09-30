package com.movetoplay.presentation.ui.component_widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes iconRes:  Int,
) {
    val background = if (selected) Color.White.copy(0.4f) else Color.Transparent
    Icon(
        painterResource(iconRes),
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick)
            .padding(12.dp)
            .size(24.dp)
    )
}