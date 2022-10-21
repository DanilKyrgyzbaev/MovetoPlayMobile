package com.movetoplay.screens.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.movetoplay.R
import com.movetoplay.databinding.ActivityMainChildBinding
import com.movetoplay.databinding.ActivityMainParentBinding
import com.movetoplay.domain.model.Child
import com.movetoplay.screens.applock.LimitationAppActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityChild : AppCompatActivity() {

    private lateinit var binding: ActivityMainChildBinding
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
            btnSettingsMainChild.setOnClickListener{
                startActivity(Intent(this@MainActivityChild,LimitationAppActivity::class.java).apply {
                    putExtra("child",Gson().toJson(child))
                })
            }
        }
    }

    private fun initViews() {
        val argument = intent.getStringExtra("child")
        if (argument != null) {
            child = Gson().fromJson(argument,Child::class.java)

            binding.tvChild.text = child.fullName
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}