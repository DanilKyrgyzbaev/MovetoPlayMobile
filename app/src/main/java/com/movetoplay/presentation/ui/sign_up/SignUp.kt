package com.movetoplay.presentation.ui.sign_up

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.CheckButton
import com.movetoplay.presentation.ui.component_widgets.EditText
import com.movetoplay.presentation.vm.session_creation.EventSessionCreation
import com.movetoplay.presentation.vm.session_creation.SessionCreationVM
import kotlin.math.abs

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUp(
    viewModel: SessionCreationVM = hiltViewModel(),
) {
    val sizeButtonAndEditText = DpSize(300.dp, 40.dp)
    val form = remember {
        viewModel.form
    }
    var iAcceptPrivacyPolicy by remember {
        mutableStateOf(true)
    }
    var message by remember {
        viewModel.message
    }
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    LaunchedEffect(message){
        viewModel.message.value?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
            message = null
        }
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
                    text = stringResource(R.string.registration),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.W600,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.weight(0.2f))
            }
            EditText(
                text = form.email,
                onValueChange = {form.email = it},
                label = stringResource(R.string.email),
                size = sizeButtonAndEditText,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Spacer(modifier = Modifier.height(12.dp))
            EditText(
                text = form.password,
                onValueChange = {form.password = it},
                label = stringResource(R.string.password),
                size = sizeButtonAndEditText,
                visualTransformation =  PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(12.dp))
            EditText(
                text = form.age?.toString() ?: "",
                onValueChange = {
                    form.age = if(it.isEmpty())
                        null
                    else
                        it.toByteOrNull()?.let { v-> abs(v.toInt()).toByte() } ?: form.age
                },
                label = stringResource(R.string.parental_age),
                size = sizeButtonAndEditText,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.width(300.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckButton(
                    value = iAcceptPrivacyPolicy,
                    onValueChange = {iAcceptPrivacyPolicy = it},
                    size = DpSize(30.dp,30.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                IAcceptPrivacyPolicyClickableText()
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                label =  stringResource(R.string.sing_up),
                onClick = { viewModel.onEvent(EventSessionCreation.SignUp)},
                size = sizeButtonAndEditText,
                enabled = iAcceptPrivacyPolicy && form.age != null
            )
        }
    }

}
