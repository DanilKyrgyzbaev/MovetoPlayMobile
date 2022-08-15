package com.movetoplay.domain.utils


sealed interface StateExercise{
    sealed class StateStarJump : StateExercise{
        object Passive : StateStarJump()
        object Star : StateStarJump()
    }
    sealed class StateSquats : StateExercise{
        object Passive : StateSquats()
        object Bottom : StateSquats()
    }
    sealed class StatePushups : StateExercise{
        object Passive : StateSquats()
        object Bottom : StateSquats()
    }
    object Undefined : StateExercise
}