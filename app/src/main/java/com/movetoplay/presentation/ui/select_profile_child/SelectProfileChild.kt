package com.movetoplay.presentation.ui.select_profile_child

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.LoadingContent
import com.movetoplay.presentation.vm.select_profile_child.EventSelectProfileChild
import com.movetoplay.presentation.vm.select_profile_child.SelectProfileChildVM

@Composable
fun SelectProfileChild(
    showContent: ()-> Unit,
    viewModel: SelectProfileChildVM = hiltViewModel()
) {
    val sizeWidgets = DpSize(300.dp, 40.dp)
    val state by remember {
        viewModel.state
    }
    LaunchedEffect(viewModel.isSelectedProfileChild.value){
        if (viewModel.isSelectedProfileChild.value)
            showContent()
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(46.dp))
        Text(
            text = stringResource(R.string.choose_child_profile),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.width(sizeWidgets.width)
        )
        Spacer(modifier = Modifier.height(20.dp))
        if(state != null){
            ProfileChildContent(
                listNameChild = viewModel.listNameChild,
                state = state!!,
                onSelectProfileChild = { index->
                    viewModel.onEvent(EventSelectProfileChild.SelectProfileChild(index))
                },
                sizeWidgets = sizeWidgets,
                addNewChild = {
                    viewModel.onEvent(EventSelectProfileChild.AddNewProfileChild)
                },
                profileSelected = {
                    viewModel.onEvent(EventSelectProfileChild.ProfileSelected)
                }
            )
        }else{
            LoadingContent()
        }

    }
}