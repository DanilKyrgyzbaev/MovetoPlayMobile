package com.movetoplay.domain.use_case.analysis_exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.movetoplay.domain.utils.StateExercise

class DeterminePoseSquats {
    operator fun invoke(
        pose: Pose
    ) : StateExercise {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP) ?: return StateExercise.Undefined
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP) ?: return StateExercise.Undefined
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)?: return StateExercise.Undefined
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)?: return StateExercise.Undefined
        if(
            leftKnee.inFrameLikelihood < 0.75 ||
            rightKnee.inFrameLikelihood < 0.75 ||
            leftHip.inFrameLikelihood< 0.75 ||
            rightHip.inFrameLikelihood < 0.75
        ) return StateExercise.Undefined
        if(
            calculateBottomPose(leftHip,rightHip,leftKnee,rightKnee)
        ) return StateExercise.StateSquats.Bottom
        return StateExercise.StateSquats.Passive
    }

    private fun calculateBottomPose(
        leftHip: PoseLandmark,
        rightHip: PoseLandmark,
        leftKnee: PoseLandmark,
        rightKnee: PoseLandmark
    ): Boolean{
        return leftKnee.position.y < leftHip.position.y &&  rightKnee.position.y < rightHip.position.y
    }
}