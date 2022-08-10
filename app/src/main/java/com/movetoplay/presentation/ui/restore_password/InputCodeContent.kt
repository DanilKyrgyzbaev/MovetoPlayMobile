package com.movetoplay.presentation.ui.restore_password

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button

@Composable
fun InputCodeContent(
    sizeButtonAndEditText: DpSize,
    next: (String) -> Unit
) {
    val code = rememberSaveable {
        mutableStateOf("")
    }
    InputCode(
        countChar = 4,
        onChange = code.component2()
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = stringResource(R.string.an_email_with_confirmation_code),
        color = Color.LightGray,
        fontSize = 15.sp,
        fontWeight = FontWeight.W400,
        modifier = Modifier.width(sizeButtonAndEditText.width),
        lineHeight = 15.sp
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        label =  stringResource(R.string.confirm),
        onClick = { next(code.value)},
        size = sizeButtonAndEditText,
    )
}