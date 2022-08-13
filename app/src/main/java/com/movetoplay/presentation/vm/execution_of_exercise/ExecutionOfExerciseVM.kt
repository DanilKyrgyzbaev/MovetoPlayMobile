package com.movetoplay.presentation.vm.execution_of_exercise

import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskExecutors
import com.movetoplay.domain.model.Exercise
import com.movetoplay.domain.use_case.AnalysisImageUseCase
import com.movetoplay.domain.utils.StateStarJump
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ExecutionOfExerciseVM @Inject constructor(
    private val analysisImageUseCase : AnalysisImageUseCase
): ViewModel() {
    private var state: StateStarJump = StateStarJump.Passive
    private val _exercise = mutableStateOf(Exercise("Прыжки", 0,null))
    val exercise : State<Exercise> get() = _exercise
    var analysisImage: ImageAnalysis
    init {
        val builder = ImageAnalysis.Builder()
        analysisImage = builder.build()
        analysisImage.setAnalyzer(TaskExecutors.MAIN_THREAD){ imageProxy ->
            analysisImageUseCase.processImageProxy(imageProxy){
                if(it==StateStarJump.Passive && state == StateStarJump.Star)
                    _exercise.value = _exercise.value.copy(count = _exercise.value.count+1)
                state = it
            }
        }
    }



}