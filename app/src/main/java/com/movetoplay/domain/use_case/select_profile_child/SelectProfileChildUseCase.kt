package com.movetoplay.domain.use_case.select_profile_child

data class SelectProfileChildUseCase(
    val saveProfileChild: SaveProfileChildUseCase,
    val getListChild: GetListChildUseCase
)