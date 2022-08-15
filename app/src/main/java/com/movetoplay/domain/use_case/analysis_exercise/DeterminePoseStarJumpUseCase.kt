package com.movetoplay.domain.use_case.analysis_exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.movetoplay.domain.utils.StateExercise
import kotlin.math.abs

class DeterminePoseStarJumpUseCase {

    operator fun invoke(
        pose: Pose
    ) : StateExercise {
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE) ?: return StateExercise.Undefined
        val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE) ?: return StateExercise.Undefined
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP) ?: return StateExercise.Undefined
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP) ?: return StateExercise.Undefined
        if(
            leftAnkle.inFrameLikelihood < 0.75 ||
            rightAnkle.inFrameLikelihood < 0.75 ||
            leftHip.inFrameLikelihood< 0.75 ||
            rightHip.inFrameLikelihood < 0.75
        ) return StateExercise.Undefined

        if(
            calculateBottomPose(leftAnkle, rightAnkle, leftHip, rightHip)
        ) return StateExercise.StateStarJump.Star
        return StateExercise.StateStarJump.Passive
    }


    private fun calculateBottomPose(
        leftAnkle: PoseLandmark,
        rightAnkle: PoseLandmark,
        leftHip: PoseLandmark,
        rightHip: PoseLandmark
    ): Boolean{
        val len_Ankle = calculateDistance(leftAnkle.position.x, rightAnkle.position.x)
        val len_Hip = calculateDistance(leftHip.position.x, rightHip.position.x)
        return len_Ankle > len_Hip * 2
                && leftAnkle.position.x > leftHip.position.x * 1.15
                && rightAnkle.position.x * 1.15 < rightHip.position.x
    }
    private fun calculateDistance(a: Float, b:Float): Float{
        return abs(a-b)
    }
}