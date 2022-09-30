package com.movetoplay.presentation.ui.select_profile_child

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movetoplay.R
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.Gender
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.CheckButton
import com.movetoplay.presentation.ui.component_widgets.DropdownList
import com.movetoplay.presentation.ui.component_widgets.EditText
import kotlin.math.abs

@Composable
fun CreatingProfileChild(
    child: Child,
    sizeWidgets: DpSize,
    profileSelected: ()-> Unit
) {
    val genders = listOf(
        stringResource(R.string.male),
        stringResource(R.string.female),
    )
    var name by remember {
       mutableStateOf(child.name)
    }
    var gender by remember {
        mutableStateOf(child.gender)
    }
    var age by remember {
        mutableStateOf(child.age)
    }
    var isEngagedSports by remember{
        mutableStateOf(child.isEngagedSports)
    }
    EditText(
        text = name ,
        onValueChange = {
            name = it
            child.name = name
        },
        label = stringResource(R.string.child_name),
        size = sizeWidgets,
    )
    Spacer(modifier = Modifier.height(16.dp))
    DropdownList(
        currentItem = gender.ordinal,
        onItemChange ={
            gender = Gender.values()[it]
            child.gender = gender
        } ,
        listItem = genders,
        size = sizeWidgets
    )
    Spacer(modifier = Modifier.height(16.dp))
    EditText(
        text = if(age<=0) "" else age.toString() ,
        onValueChange = {
            it.toByteOrNull()?.let { value->
                age = abs(value.toInt())
                child.age = age
            } ?: run { age = 0 }
        },
        label = stringResource(R.string.age),
        size = sizeWidgets,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.width(300.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckButton(
            value = isEngagedSports,
            onValueChange = {isEngagedSports = it},
            size = DpSize(30.dp,30.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = stringResource(R.string.does_your_child_play_sports),color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp, fontWeight = FontWeight.W400)
    }
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        label = stringResource(R.string.сontinue),
        onClick = profileSelected,
        size = sizeWidgets,
        enabled = name.isNotEmpty() && age > 0
    )
}