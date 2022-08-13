package com.movetoplay.presentation.vm.select_profile_child

import com.movetoplay.domain.model.Child

data class StateSelectProfileChild(
    val isEdit: Boolean,
    val index: Int,
    val child: Child
)