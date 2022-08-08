package com.movetoplay.presentation.ui.sign_up_widget

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.domain.model.RequestStatus
import com.movetoplay.presentation.component_widgets.CustomLoadingButton
import com.movetoplay.presentation.vm.session_creation.EventSessionCreation
import com.movetoplay.presentation.vm.session_creation.SessionCreationVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpWidget(
    sessionCreationVM: SessionCreationVM = hiltViewModel()
) {
    LaunchedEffect(Unit){
        sessionCreationVM.onEvent(EventSessionCreation.SignUpReset)
    }
    val state by remember {
        sessionCreationVM.state
    }
    val status by remember {
        sessionCreationVM.status
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var repeatPassword by remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Spacer(modifier = Modifier.weight(0.5f))
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text(text = stringResource(R.string.email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = state.isErrorEmail
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(text = stringResource(R.string.password)) },
            singleLine = true,
            visualTransformation =  PasswordVisualTransformation(),
            isError = state.isErrorPassword
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = {repeatPassword = it},
            label = { Text(text = stringResource(R.string.repeat_password)) },
            singleLine = true,
            visualTransformation =  PasswordVisualTransformation(),
            isError = state.isErrorRepeatPassword == true
        )
        Spacer(modifier = Modifier.height(18.dp))
        CustomLoadingButton(
            state = status is RequestStatus.Loading ,
            onChange = {
                sessionCreationVM.onEvent(
                    EventSessionCreation.SignUp(email = email, password = password, repeatPassword = repeatPassword)
                )
            } ,
            label =  stringResource(R.string.сontinue),
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}