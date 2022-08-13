package com.movetoplay.presentation.vm.app_vm

sealed class StateUserApp {
    object Definition : StateUserApp()
    object NotAuthorized : StateUserApp()
    object Children : StateUserApp()
    object Parent : StateUserApp()
}