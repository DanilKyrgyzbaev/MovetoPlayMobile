package com.movetoplay.screens.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.movetoplay.databinding.ActivityChildInfoBinding
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.pref.Pref
import com.movetoplay.screens.applock.ApkInfoExtractor
import com.movetoplay.screens.applock.LimitationAppActivity
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChildInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildInfoBinding
    private val vm: ChildInfoViewModel by viewModels()
    private var childInfo = ChildInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildInfoBinding.inflate(layoutInflater)
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
        vm.getUserApps(Pref.childId)
    }

    private fun setupListeners() {
        binding.run {
            btnSettingsMainChild.setOnClickListener {
                startActivity(
                    Intent(
                        this@ChildInfoActivity,
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
        vm.userApps.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.progressChild.visible(true)
                }
                is ResultStatus.Success -> {
                    binding.progressChild.visible(false)
                    it.data?.let { data ->
                        setUserApps(data)
                    }
                }
                is ResultStatus.Error -> {
                    binding.progressChild.visible(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUserApps(apps: List<UserApp>) {
        val userApps = ArrayList<UserApp>()
        userApps.addAll(apps)
        if (apps.isNotEmpty()) {
            Log.e("childInfo", "setUserApps: $apps")
            apps.forEachIndexed { index, app ->
                userApps[index].drawable =
                    ApkInfoExtractor(this).getAppIconByPackageName(app.packageName)
            }
        }
        binding.rvChildInfo.adapter = ChildInfoAdapter(userApps)
    }

    private fun setChildInfo(info: ChildInfo) {
        Log.e("mainChild", "setChildInfo: info:${info.needJumpCount}  pref:${ExercisesPref.jumps}")
        binding.run {
            (info.needJumpCount ?: ExercisesPref.jumps).let {
                progressJump.max = it
                "/$it".also { jumps -> tvJumpDefault.text = jumps }
            }
            (info.needSquatsCount ?: ExercisesPref.squats).let {
                progressSquats.max = it
                "/$it".also { s -> tvSquatsDefault.text = s }
            }
            (info.needSqueezingCount ?: ExercisesPref.squeezing).let {
                progressSqueezing.max = it
                "/$it".also { sqz -> tvSqueezingDefault.text = sqz }
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