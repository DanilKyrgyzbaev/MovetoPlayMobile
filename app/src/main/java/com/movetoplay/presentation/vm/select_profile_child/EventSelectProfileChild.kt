package com.movetoplay.presentation.vm.select_profile_child

sealed class EventSelectProfileChild {
    class SelectProfileChild(val index: Int) : EventSelectProfileChild()
    object AddNewProfileChild :  EventSelectProfileChild()
    object ProfileSelected : EventSelectProfileChild()
}