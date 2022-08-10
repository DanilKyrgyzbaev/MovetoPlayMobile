package com.movetoplay.presentation.ui.restore_password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.EditText

@Composable
fun InputEmailContent(
    sizeButtonAndEditText: DpSize,
    next: (String) -> Unit
) {
    val email = remember {
        mutableStateOf("")
    }
    EditText(
        text = email.value,
        onValueChange = email.component2(),
        label = stringResource(R.string.email),
        size = sizeButtonAndEditText,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = stringResource(R.string.input_email),
        color = Color.LightGray,
        fontSize = 15.sp,
        fontWeight = FontWeight.W400,
        modifier = Modifier.width(sizeButtonAndEditText.width),
        lineHeight = 15.sp
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        label =  stringResource(R.string.сontinue),
        onClick = { next(email.value)},
        size = sizeButtonAndEditText,
    )
}