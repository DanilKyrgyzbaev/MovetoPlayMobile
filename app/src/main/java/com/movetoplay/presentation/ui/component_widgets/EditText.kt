package com.movetoplay.presentation.ui.component_widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditText(
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    size: DpSize,
    editable:Boolean = true,
    isError : Boolean = false,
    textSize: TextUnit = 18.sp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
    shape : RoundedCornerShape = RoundedCornerShape(10.dp)
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        enabled = editable ,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(colorScheme.primary.copy(0.9f)),
        textStyle = TextStyle(color = colorScheme.primary, textAlign = TextAlign.Start, fontSize = textSize),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .size(size)
                    .border(
                        BorderStroke(1.5.dp, colorScheme.primary),
                        shape
                    ).padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ){
                if (text.isEmpty())
                    Text(text = label, color = colorScheme.primary.copy(0.8f),
                        fontSize = textSize, fontWeight = FontWeight.W400)
                innerTextField()
            }
        }
    )
}