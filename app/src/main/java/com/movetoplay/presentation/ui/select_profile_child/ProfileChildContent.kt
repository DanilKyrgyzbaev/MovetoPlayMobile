package com.movetoplay.presentation.ui.select_profile_child

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movetoplay.R
import com.movetoplay.presentation.ui.component_widgets.Button
import com.movetoplay.presentation.ui.component_widgets.DropdownList
import com.movetoplay.presentation.vm.select_profile_child.EventSelectProfileChild
import com.movetoplay.presentation.vm.select_profile_child.SelectProfileChildVM
import com.movetoplay.presentation.vm.select_profile_child.StateSelectProfileChild
import com.movetoplay.presentation.vm.session_creation.SessionCreationVM

@Composable
fun ProfileChildContent(
    viewModel: SelectProfileChildVM = hiltViewModel(),
    listNameChild: List<String> ,
    state: StateSelectProfileChild,
    onSelectProfileChild: (Int)->Unit,
    sizeWidgets: DpSize,
    addNewChild: ()-> Unit,
    profileSelected: ()->Unit
) {
    DropdownList(
        currentItem = state.index,
        onItemChange = onSelectProfileChild,
        listItem = listNameChild,
        size = sizeWidgets,
        addNewItem = addNewChild,
        labelAddNewItem = stringResource(R.string.create_new,R.string.import_old)
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (state.isEdit){
        CreatingProfileChild(
            child = state.child,
            sizeWidgets = sizeWidgets,
            profileSelected = profileSelected
        )
    }else{
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            label = stringResource(R.string.сontinue),
            onClick = {viewModel.onEvent(EventSelectProfileChild.ProfileSelected)},
            size = sizeWidgets,
        )
    }
}