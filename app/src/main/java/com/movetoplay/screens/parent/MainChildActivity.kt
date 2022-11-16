package com.movetoplay.screens.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.movetoplay.databinding.ActivityMainChildBinding
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.pref.Pref
import com.movetoplay.screens.applock.LimitationAppActivity
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainChildActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainChildBinding
    private val vm: MainParentViewModel by viewModels()
    private var childInfo = ChildInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainChildBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        val argument = intent.getStringExtra("child")
        if (argument != null) {
            val child = Gson().fromJson(argument, Child::class.java)

            binding.tvChild.text = child.fullName
            Pref.childId = child.id
        }
        vm.getChildInfo()
        vm.getDailyExercises(Pref.childId)
    }

    private fun setupListeners() {
        binding.run {
            btnSettingsMainChild.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainChildActivity,
                        LimitationAppActivity::class.java
                    ).apply {
                        putExtra("childInfo", Gson().toJson(childInfo))
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
        vm.childInfoResult.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.progressChild.visible(true)
                }
                is ResultStatus.Success -> {
                    binding.progressChild.visible(false)
                    it.data?.let { data ->
                        setChildInfo(data)
                        childInfo = data
                    }
                }
                is ResultStatus.Error -> {
                    binding.progressChild.visible(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setChildInfo(info: ChildInfo) {
        Log.e("mainChild", "setChildInfo: info:${info.needJumpCount}  pref:${ExercisesPref.jumps}")
        binding.run {
            (info.needJumpCount ?: ExercisesPref.jumps).let {
                progressJump.max = it
                tvJumpDefault.text = "/$it"
            }
            (info.needSquatsCount ?: ExercisesPref.squats).let {
                progressSquats.max = it
                tvSquatsDefault.text = "/$it"
            }
            (info.needSqueezingCount ?: ExercisesPref.squeezing).let {
                progressSqueezing.max = it
                tvSqueezingDefault.text = "/$it"
            }
        }
    }

    private fun setData(exercises: DailyExercises) {
        binding.run {
            //jumps
            tvJump.text = exercises.jumps?.count.toString()
            progressJump.progress = exercises.jumps?.count ?: ExercisesPref.jumps
            //squats
            tvSquats.text = exercises.squats?.count.toString()
            progressSquats.progress = exercises.squats?.count ?: ExercisesPref.squats

            //squeezing
            tvSqueezing.text = exercises.squeezing?.count.toString()
            progressSqueezing.progress = exercises.squeezing?.count ?: ExercisesPref.squeezing
        }
    }

    override fun onRestart() {
        vm.getDailyExercises(Pref.childId)
        vm.getChildInfo()
        Log.e("mainChild", "onRestart: is working")
        super.onRestart()
    }

    override fun onBackPressed() {
        resetData()
        finish()
        super.onBackPressed()
    }

    private fun resetData() {
        Pref.childToken = ""
        Pref.deviceId = ""
        Pref.childId = ""
    }
}