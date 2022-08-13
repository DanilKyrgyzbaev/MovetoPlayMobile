package com.movetoplay.presentation.ui.restore_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.EditText

@Composable
fun ChangePasswordContent(
    sizeButtonAndEditText: DpSize,
    next: (newPassword: String, repeatPassword: String) -> Unit
) {
    val newPassword = remember {
        mutableStateOf("")
    }
    val repeatPassword = remember {
        mutableStateOf("")
    }
    EditText(
        text = newPassword.value,
        onValueChange = newPassword.component2(),
        label = stringResource(R.string.new_password),
        size = sizeButtonAndEditText,
        visualTransformation =  PasswordVisualTransformation(),
    )
    Spacer(modifier = Modifier.height(12.dp))
    EditText(
        text = repeatPassword.value,
        onValueChange = repeatPassword.component2(),
        label = stringResource(R.string.repeat_password),
        size = sizeButtonAndEditText,
        visualTransformation =  PasswordVisualTransformation(),
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        label =  stringResource(R.string.save),
        onClick = {next(newPassword.value,repeatPassword.value) },
        size = sizeButtonAndEditText,
    )
}