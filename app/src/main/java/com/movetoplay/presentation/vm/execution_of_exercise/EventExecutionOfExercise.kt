package com.movetoplay.presentation.vm.execution_of_exercise

import com.movetoplay.domain.model.TypeExercise

sealed class EventExecutionOfExercise {
    class InitTypeExercise(val type:TypeExercise) : EventExecutionOfExercise()
}