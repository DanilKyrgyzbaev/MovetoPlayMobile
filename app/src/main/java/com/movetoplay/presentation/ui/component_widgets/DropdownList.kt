package com.movetoplay.presentation.ui.component_widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun DropdownList(
    currentItem: Int,
    onItemChange: (Int)-> Unit,
    listItem: List<String>,
    size: DpSize,
    addNewItem: (()-> Unit)? = null,
    labelAddNewItem: String = "",
    textSize: TextUnit = 18.sp,
    shape : RoundedCornerShape = RoundedCornerShape(10.dp)
) {
    var expanded by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    var textfieldSize by remember { mutableStateOf(Size.Zero)}
    Box(
        modifier = Modifier
            .size(size)
            .border(
                BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                shape
            )
            .clip(shape)
            .clickable { expanded = !expanded }
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    textfieldSize = coordinates.size.toSize()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = listItem.getOrNull(currentItem) ?: labelAddNewItem,
                color = MaterialTheme.colorScheme.primary,
                fontSize = textSize,
                fontWeight = FontWeight.W600
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier
                    .rotate(rotate),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp()})
                .background(MaterialTheme.colorScheme.background),
            offset = DpOffset(0.dp,8.dp)
        ) {
            for (i in listItem.indices){
                DropdownMenuItem(
                    text = {
                        Text(
                            text = listItem[i],
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = textSize,
                            fontWeight = FontWeight.W600
                        )
                    },
                    onClick = {
                        onItemChange(i)
                        expanded = !expanded
                    },
                    modifier = Modifier.height(with(LocalDensity.current) { textfieldSize.height.toDp()})
                )
            }
            addNewItem?.let {
                if(listItem.isNotEmpty())
                    Divider(
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = Color.LightGray
                    )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = labelAddNewItem,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = textSize,
                            fontWeight = FontWeight.W600
                        )
                    },
                    onClick = {
                        it()
                        expanded = !expanded
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    modifier = Modifier.height(with(LocalDensity.current) { textfieldSize.height.toDp()})
                )
            }
        }
    }
}