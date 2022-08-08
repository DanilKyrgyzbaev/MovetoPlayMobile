package com.movetoplay.presentation.ui.first_time_widget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.domain.model.RequestStatus
import com.movetoplay.presentation.component_widgets.CustomLoadingButton
import com.movetoplay.presentation.vm.session_creation.EventSessionCreation
import com.movetoplay.presentation.vm.session_creation.SessionCreationVM

@Composable
fun FirstTimeWidget(
    signInViaGoogle: () -> Unit,
    signUp : ()-> Unit,
    signIn: ()-> Unit,
    sessionCreationVM: SessionCreationVM = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val status by remember {
            sessionCreationVM.status
        }
        CustomLoadingButton(
            state = status is RequestStatus.Loading,
            onChange ={
               if(it){
                   sessionCreationVM.onEvent(EventSessionCreation.SignInGoogle)
               }else {
                   sessionCreationVM.onEvent(EventSessionCreation.SignInGoogleReset)
               }
            },
            icon = painterResource(R.drawable.ic_google),
            label = stringResource(R.string.sign_in_via_google),
        )
        Spacer(modifier = Modifier.height(12.dp))
        ElevatedButton(
            onClick = signUp
        ) {
            Text(text = stringResource(R.string.sign_up))
        }
        Spacer(modifier = Modifier.height(12.dp))
        ElevatedButton(
            onClick = signIn
        ) {
            Text(text = stringResource(R.string.sign_in_via_mail))
        }
    }
}