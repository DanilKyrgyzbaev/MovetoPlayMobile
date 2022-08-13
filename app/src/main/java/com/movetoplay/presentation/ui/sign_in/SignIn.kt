package com.movetoplay.presentation.ui.sign_in

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.domain.model.Role
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.CheckButton
import com.movetoplay.presentation.ui.component_widgets.EditText
import com.movetoplay.presentation.vm.session_creation.EventSessionCreation
import com.movetoplay.presentation.vm.session_creation.SessionCreationVM
import com.movetoplay.presentation.vm.session_creation.StateSessionCreation

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignIn(
    viewModel: SessionCreationVM = hiltViewModel(),
    retrievePassword : ()-> Unit,
    sessionCreated: ()->Unit
) {
    val sizeButtonAndEditText = DpSize(300.dp, 40.dp)
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var role by remember {
        mutableStateOf(Role.Children)
    }
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    LaunchedEffect(viewModel.message.value){
        viewModel.message.value?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }
    LaunchedEffect(viewModel.state.value){
        if(viewModel.state.value == StateSessionCreation.Success)
            sessionCreated()
    }
    Scaffold(scaffoldState = scaffoldState) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier.fillMaxHeight(0.5f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.weight(0.8f))
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = "logo",
                    modifier = Modifier.size(100.dp),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.log_in),
                    color = colorScheme.primary,
                    fontWeight = FontWeight.W600,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.weight(0.2f))
            }
            EditText(
                text = email,
                onValueChange = {email = it},
                label = stringResource(R.string.email),
                size = sizeButtonAndEditText,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Spacer(modifier = Modifier.height(12.dp))
            EditText(
                text = password,
                onValueChange = {password = it},
                label = stringResource(R.string.password),
                size = sizeButtonAndEditText,
                visualTransformation =  PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.width(300.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckButton(
                    value = role == Role.Children,
                    onValueChange = {role = if(it) Role.Children else Role.Parent},
                    size = DpSize(30.dp,30.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(R.string.child_phone),color = colorScheme.primary,
                    fontSize = 18.sp, fontWeight = FontWeight.W400)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                label =  stringResource(R.string.sign_in),
                onClick = { viewModel.onEvent(EventSessionCreation.SignIn(email, password, role)) },
                size = sizeButtonAndEditText,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ClickableText(
                text = AnnotatedString(stringResource(R.string.forgot_password)),
                onClick = {
                    retrievePassword()
                },
                style = TextStyle(
                    color = colorScheme.primary,
                    fontSize =  18.sp,
                    fontWeight = FontWeight.W600
                )
            )
        }
    }
}