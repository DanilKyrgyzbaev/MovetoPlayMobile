package com.movetoplay.presentation.vm.app_vm

sealed class EventApp {
    object UserAuth : EventApp()
}