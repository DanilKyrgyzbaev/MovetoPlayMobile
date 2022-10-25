package com.movetoplay.pref

import com.movetoplay.App

object ExercisesPref {
    private val sharedPreferences
        get() = App.sharedPrefs

    var dailyExercisesCount: Int
        get() = sharedPreferences.getInt("daily_exercise_count", 30)
        set(value) = sharedPreferences.edit().putInt("daily_exercise_count", value).apply()


}
