package com.movetoplay.presentation.ui.splash_screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.movetoplay.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(R.drawable.logotype),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.6f)
        )
        LaunchedEffect(key1 = true,){
            delay(5000L)
        }
//        lifecycleScope.launchWhenCreated {
//            delay(3000)
//
//            val intent = Intent(this@SplashActivity, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }
}

