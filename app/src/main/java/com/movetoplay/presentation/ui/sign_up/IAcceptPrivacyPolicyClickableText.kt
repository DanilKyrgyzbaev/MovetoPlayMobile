package com.movetoplay.presentation.ui.sign_up

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.movetoplay.R


@Composable
fun IAcceptPrivacyPolicyClickableText() {
    val annotatedText = buildAnnotatedString {
        append("${stringResource(R.string.i_accept)} ")
        pushStringAnnotation(tag = "URL",
            annotation = "https://developer.android.com")
        withStyle(
            style = SpanStyle(textDecoration = TextDecoration.Underline)
        ) {
            append(stringResource(R.string.privacy_policy))
        }
        pop()
    }
    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL",
                start = offset,
                end = offset)
                .firstOrNull()?.let { annotation ->
                }
        },
        style = TextStyle(
            color = colorScheme.primary,
            fontSize = 18.sp, fontWeight = FontWeight.W400)
    )
}