package com.movetoplay.presentation.ui.first_time

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.TypeButton


@Composable
fun FirstTime(
    signUp : ()-> Unit,
    signIn: ()-> Unit,
) {
    val sizeButton = DpSize(300.dp, 40.dp)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.BottomCenter
        ){
            Image(
                painter = painterResource(R.drawable.logotype),
                contentDescription = "logo",
                modifier = Modifier.fillMaxSize(0.7f)
            )
        }
        Button(
            label = stringResource(R.string.sign_in_via_google) ,
            onClick = {  },
            size = sizeButton
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            label = stringResource(R.string.sign_in_via_mail) ,
            onClick = signIn,
            size = sizeButton,
            typeButton = TypeButton.Outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString(stringResource(R.string.no_have_account)),
            onClick = {
                signUp()
            },
            style = TextStyle(
                color = colorScheme.primary,
                fontSize =  18.sp,
                fontWeight = FontWeight.W600
            )
        )
        Spacer(modifier = Modifier.height(80.dp))
    }
}