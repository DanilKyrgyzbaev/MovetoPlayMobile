package com.movetoplay.presentation.vm.execution_of_exercise

import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskExecutors
import com.movetoplay.domain.model.Exercise
import com.movetoplay.domain.use_case.analysis_exercise.AnalysisImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExecutionOfExerciseVM @Inject constructor(
    private val analysisImageUseCase: AnalysisImageUseCase
) : ViewModel() {
    private lateinit var _exercise: MutableState<Exercise>
    val exercise: State<Exercise> get() = _exercise
    private val _isInit = mutableStateOf(false)
    val isInit: State<Boolean> get() = _isInit
    lateinit var analysisImage: ImageAnalysis
    private fun initializingState() {
        val builder = ImageAnalysis.Builder()
        analysisImage = builder.build()
        analysisImage.setAnalyzer(TaskExecutors.MAIN_THREAD) { imageProxy ->
//            analysisImageUseCase.processImageProxy(imageProxy, _exercise.value.type)
        }
    }

    fun onEvent(event: EventExecutionOfExercise) {
        when (event) {
            is EventExecutionOfExercise.InitTypeExercise -> {
                _exercise = mutableStateOf(Exercise(event.type, 0, null))
                initializingState()
                _isInit.value = true
            }
        }
    }
}
