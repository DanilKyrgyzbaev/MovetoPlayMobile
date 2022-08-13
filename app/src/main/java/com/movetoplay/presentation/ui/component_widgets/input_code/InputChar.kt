package com.movetoplay.presentation.ui.component_widgets.input_code

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputChar(
    char: String,
    onCharChange: (String)-> Unit,
    goNextInputChar: ()-> Unit,
    goBackInputChar: ()-> Unit,
    size: DpSize,
    focus: FocusRequester,
    textSize: TextUnit = 18.sp,
    shape : RoundedCornerShape = RoundedCornerShape(10.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
) {
    BasicTextField(
        value = char,
        onValueChange = {
            if(it.length <=1)
                onCharChange(it)
            if (it.isNotEmpty())
                goNextInputChar()
            else
                goBackInputChar()
        },
        singleLine = true,
        modifier = Modifier.focusRequester(focus),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(colorScheme.primary.copy(0.9f)),
        textStyle = TextStyle(color = colorScheme.primary, textAlign = TextAlign.Center, fontSize = textSize,),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .size(size)
                    .border(
                        BorderStroke(1.5.dp, colorScheme.primary),
                        shape
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ){
                if (char.isEmpty())
                    Box(modifier = Modifier
                        .height(2.dp)
                        .width(7.dp)
                        .background(colorScheme.primary, CircleShape)
                    )
                innerTextField()
            }
        }
    )
}