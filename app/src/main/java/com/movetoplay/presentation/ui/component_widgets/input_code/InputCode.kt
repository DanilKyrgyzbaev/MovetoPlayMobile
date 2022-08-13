package com.movetoplay.presentation.ui.component_widgets.input_code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun InputCode(
    countChar: Int,
    onChange: (String)->Unit
){
    val listFocus = remember {
        Array(countChar){ FocusRequester() }.toList()
    }
    val code = remember {
        mutableStateMapOf<Int,String>()
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        for (i in 0 until countChar){
            InputChar(
                char = code.getOrDefault(i,""),
                onCharChange = {
                    code[i] = it
                    onChange(code.values.joinToString(""))
                },
                goNextInputChar = {listFocus.getOrNull(i+1)?.requestFocus()},
                goBackInputChar =  {listFocus.getOrNull(i-1)?.requestFocus()},
                focus = listFocus[i],
                size = DpSize(35.dp,50.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}