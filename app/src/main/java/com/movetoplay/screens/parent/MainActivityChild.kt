package com.movetoplay.screens.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.movetoplay.databinding.ActivityMainChildBinding
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.pref.Pref
import com.movetoplay.screens.applock.LimitationAppActivity
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityChild : AppCompatActivity() {

    private lateinit var binding: ActivityMainChildBinding
    private val vm: MainParentViewModel by viewModels()
    private var child = Child()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainChildBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setupListeners()
    }

    private fun setupListeners() {
        binding.run {
            btnSettingsMainChild.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivityChild,
                        LimitationAppActivity::class.java
                    ).apply {
                        putExtra("child", Gson().toJson(child))
                    })
            }
        }

        vm.dailyExerciseResult.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.progressChild.visible(true)
                }
                is ResultStatus.Success -> {
                    binding.progressChild.visible(false)
                    it.data?.let { data -> setData(data) }
                }
                is ResultStatus.Error -> {
                    binding.progressChild.visible(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(exercises: DailyExercises) {
        val dailyCount = ExercisesPref.dailyExercisesCount
        binding.run {
            //jumps
            "${exercises.jumps.count}/$dailyCount".also {
                tvJump.text = it
            }
            progressJump.max = dailyCount
            progressJump.progress = exercises.jumps.count
            //squats
            "${exercises.squats.count}/$dailyCount".also {
                tvSquats.text = it
            }
            progressSquats.max = dailyCount
            progressSquats.progress = exercises.squats.count

            //squeezing
            "${exercises.squeezing.count}/$dailyCount".also {
                tvSqueezing.text = it
            }
            progressSqueezing.max = dailyCount
            progressSqueezing.progress = exercises.squeezing.count
        }
    }

    private fun initViews() {
        val argument = intent.getStringExtra("child")
        if (argument != null) {
            child = Gson().fromJson(argument, Child::class.java)

            binding.tvChild.text = child.fullName
            Pref.childId = child.id
        }
        vm.checkDailyExercise()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}