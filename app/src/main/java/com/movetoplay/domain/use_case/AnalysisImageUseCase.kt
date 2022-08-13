package com.movetoplay.domain.use_case

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.movetoplay.domain.utils.StateStarJump
import java.util.concurrent.Executors


class AnalysisImageUseCase(
    private val useCase: DeterminePoseStarJumpUseCase
){
    private val executor = Executors.newSingleThreadExecutor()
    private val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    private val poseDetector = PoseDetection.getClient(options)


    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    fun processImageProxy(image: ImageProxy, onChangeStateStarJump: (StateStarJump)-> Unit) {
        image.image?.let {
            poseDetector.process(InputImage.fromMediaImage(it, image.imageInfo.rotationDegrees))
                .addOnSuccessListener(executor) { results: Pose ->
                    onChangeStateStarJump(useCase(results))
                }
                .addOnFailureListener(executor) { e: Exception ->
                    Log.e("Camera", "Error detecting pose", e)
                }
                .addOnCompleteListener { image.close() }
        } ?: image.close()

    }


}