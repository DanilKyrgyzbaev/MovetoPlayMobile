package com.movetoplay.presentation.ui.component_widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TypeButton{
    Outline,
    Pouring
}

@Composable
fun Button(
    label: String,
    onClick: ()-> Unit,
    size: DpSize,
    textSize: TextUnit = 18.sp,
    enabled: Boolean = true,
    typeButton: TypeButton = TypeButton.Pouring,
    shape : RoundedCornerShape = RoundedCornerShape(10.dp)
) {
    val contentColor =  if (typeButton == TypeButton.Outline)
        colorScheme.primary else Color.White
    val colorBackground = if (typeButton == TypeButton.Outline)
        colorScheme.background else colorScheme.primary

    val modifier = when(typeButton){
        TypeButton.Outline ->{
            Modifier
                .size(size)
                .clip(shape)
                .background(colorBackground)
                .border(BorderStroke(1.5.dp, colorScheme.primary),shape)
                .clickable(enabled = enabled, onClick = onClick)
        }
        TypeButton.Pouring -> {
            Modifier
                .size(size)
                .clip(shape)
                .background(
                    if(enabled)
                        colorBackground
                    else
                        colorBackground.copy(0.8f)
                ).clickable(enabled = enabled, onClick = onClick)
        }
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = label,
            color = if(enabled) contentColor else contentColor.copy(0.8f),
            fontSize = textSize,
            fontWeight = FontWeight.W600
        )
    }

}

@Preview
@Composable
fun PreviewButton() {
    Button(
        label = "Вход",
        onClick= {},
        typeButton = TypeButton.Outline,
        size = DpSize(300.dp, 40.dp)
    )
}