package com.movetoplay.domain.utils

sealed class StateStarJump{
    object Passive : StateStarJump()
    object Star : StateStarJump()
    object Undefined : StateStarJump()

    override fun toString(): String {
        return when(this){
            Passive -> "Passive"
            Star -> "Star"
            Undefined -> "Undefined"
        }
    }
}