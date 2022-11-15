package com.movetoplay.pref

import com.movetoplay.App

object ExercisesPref {
    private val sharedPreferences
        get() = App.sharedPrefs

    var jumps: Int
        get() = sharedPreferences.getInt("daily_exercise_count_jumps", 15)
        set(value) = sharedPreferences.edit().putInt("daily_exercise_count_jumps", value).apply()
    var seconds: Int
        get() = sharedPreferences.getInt("daily_exercise_seconds", 60)
        set(value) = sharedPreferences.edit().putInt("daily_exercise_seconds", value).apply()
    var squats: Int
        get() = sharedPreferences.getInt("daily_exercise_count_squats", 15)
        set(value) = sharedPreferences.edit().putInt("daily_exercise_count_squats", value).apply()
    var squeezing: Int
        get() = sharedPreferences.getInt("daily_exercise_count_squeezing", 15)
        set(value) = sharedPreferences.edit().putInt("daily_exercise_count_squeezing", value).apply()
}
