package com.movetoplay.domain.use_case.analysis_exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.movetoplay.domain.utils.StateExercise
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class DeterminePosePushups {
    operator fun invoke(
        pose: Pose
    ): StateExercise {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)?: return StateExercise.Undefined
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)?: return StateExercise.Undefined
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)?: return StateExercise.Undefined
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)?: return StateExercise.Undefined
        if(
            leftShoulder.inFrameLikelihood < 0.75 ||
            rightShoulder.inFrameLikelihood < 0.75 ||
            leftWrist.inFrameLikelihood< 0.75 ||
            rightWrist.inFrameLikelihood < 0.75
        ) return StateExercise.Undefined
        if (
            calculateTopPose(leftShoulder, rightShoulder, leftWrist, rightWrist)
        ) return StateExercise.StatePushups.Bottom
        return StateExercise.StatePushups.Passive
    }
    private fun calculateTopPose(
        leftShoulder:PoseLandmark,
        rightShoulder: PoseLandmark,
        leftWrist: PoseLandmark,
        rightWrist: PoseLandmark
    ) : Boolean{
        val vectorA = (leftShoulder.position.x - rightWrist.position.x) to (leftShoulder.position.y - rightWrist.position.y)
        val vectorB = (rightShoulder.position.x - leftWrist.position.x) to (rightShoulder.position.y - leftWrist.position.y)
        val bott = sqrt(vectorA.first.pow(2) + vectorA.second.pow(2)) * sqrt(vectorB.first.pow(2) + vectorB.second.pow(2))
        val angle = (vectorA.first * vectorB.first+ vectorA.second * vectorB.second) / bott
        return acos(angle) > 1.7
    }
}