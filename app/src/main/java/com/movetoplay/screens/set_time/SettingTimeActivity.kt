package com.movetoplay.screens.set_time

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.movetoplay.databinding.ActivitySettingTimeBinding
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingTimeActivity : AppCompatActivity() {

    private lateinit var ui: ActivitySettingTimeBinding
    private val vm: SetTimeViewModel by viewModels()
    private lateinit var childInfo: ChildInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySettingTimeBinding.inflate(layoutInflater)
        setContentView(ui.root)

        initViews()
        initListeners()
    }

    private fun initViews() {
        val argument = intent.getStringExtra("childInfo")
        if (argument != null) {
            childInfo = Gson().fromJson(argument, ChildInfo::class.java)
            ui.etJumps.hint = (childInfo.needJumpCount?: ExercisesPref.jumps).toString()
            ui.etSquats.hint = (childInfo.needSquatsCount?:ExercisesPref.squats).toString()
            ui.etSqueezing.hint = (childInfo.needSqueezingCount?:ExercisesPref.squeezing).toString()
            ui.etLimit.hint = childInfo.needSeconds?: ExercisesPref.seconds.toString()
        }
    }

    private fun initListeners() {
        ui.apply {
            buttonSet.setOnClickListener {
                val timeStr: String = etLimit.text.toString()
                val time = timeStr.ifEmpty { (childInfo.needSeconds
                    ?: ExercisesPref.seconds).toString() }
                val jumps = etJumps.text.toString()
                val squats = etSquats.text.toString()
                val squeezing = etSqueezing.text.toString()

                if (squats.isEmpty() && jumps.isEmpty() && squeezing.isEmpty()) goTo()
                else {
                    vm.updateLimitations(
                        time.toInt(),
                        if (squats.isEmpty()) (childInfo.needSquatsCount?:ExercisesPref.squats)
                        else squats.toInt(),
                        if (jumps.isEmpty()) (childInfo.needJumpCount?: ExercisesPref.jumps)
                        else jumps.toInt(),
                        if (squeezing.isEmpty()) childInfo.needSqueezingCount?:ExercisesPref.squeezing
                        else squeezing.toInt()
                    )
                }
            }
        }
        vm.updateResultStatus.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    ui.progress.visible(true)
                }
                is ResultStatus.Error -> {
                    ui.progress.visible(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                }
                is ResultStatus.Success -> {
                    ui.progress.visible(false)
                    goTo()
                }
            }
        }
    }

    private fun goTo() {
        finish()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
