package com.movetoplay.presentation.ui.restore_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.presentation.vm.restore_password.EventRestorePassword
import com.movetoplay.presentation.vm.restore_password.RestorePasswordVM
import com.movetoplay.presentation.vm.restore_password.StateRestorePassword

@Composable
fun RestorePassword(
    viewModel: RestorePasswordVM = hiltViewModel()
) {
    val state by remember { viewModel.state }
    val sizeButtonAndEditText = DpSize(300.dp, 40.dp)
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
                text = if(state == StateRestorePassword.ChangePassword)
                    stringResource(R.string.change_password)
                else
                    stringResource(R.string.restore_password),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                fontSize = 32.sp,
                modifier = Modifier.width(sizeButtonAndEditText.width)
            )
            Spacer(modifier = Modifier.weight(0.2f))
        }
        when(state){
            StateRestorePassword.InputEmail -> InputEmailContent(sizeButtonAndEditText){
                viewModel.onEvent(EventRestorePassword.SendEmail(it))
            }
            StateRestorePassword.InputCode -> InputCodeContent(sizeButtonAndEditText){
                viewModel.onEvent(EventRestorePassword.SendCode(it))
            }
            StateRestorePassword.ChangePassword -> ChangePasswordContent(sizeButtonAndEditText){
                    newPassword, repeatPassword ->
                viewModel.onEvent(EventRestorePassword.SendNewPassword(newPassword, repeatPassword))
            }
        }
    }
}