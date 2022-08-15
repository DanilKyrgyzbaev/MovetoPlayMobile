package com.movetoplay.presentation.vm.app_vm

sealed class StateUserApp {
    object Definition : StateUserApp()
    object NotAuthorized : StateUserApp()
    class Children(val selectedProfileChild : Boolean) : StateUserApp()
    object Parent : StateUserApp()
}