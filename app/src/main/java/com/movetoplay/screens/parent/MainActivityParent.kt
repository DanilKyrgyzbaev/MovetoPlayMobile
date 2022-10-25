package com.movetoplay.screens.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.movetoplay.databinding.ActivityMainParentBinding
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.screens.create_child_profile.ListChildesAdapter
import com.movetoplay.screens.create_child_profile.SetupProfileActivity
import com.movetoplay.screens.create_child_profile.SetupProfileViewModel
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityParent : AppCompatActivity() {

    private lateinit var binding: ActivityMainParentBinding
    private val vm: SetupProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        initViews()
    }

    private fun initViews() {
        vm.getChildesList()
    }

    private fun initListeners() {
        vm.getChildesResultStatus.observe(this) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.pbChooseChild.visible(true)
                }
                is ResultStatus.Success -> {
                    binding.pbChooseChild.visible(false)
                    setData(it.data)
                }
                is ResultStatus.Error -> {
                    binding.pbChooseChild.visible(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnCreateChooseChild.setOnClickListener {
            startActivity(Intent(this,SetupProfileActivity::class.java))
            finish()
        }
    }

    private fun setData(list: List<Child>?) {
        if (list?.isEmpty() == true) {
            Toast.makeText(
                this,
                "У вас нет профилей ребенка, создайте новый с устройства ребенка",
                Toast.LENGTH_LONG
            ).show()
        } else  binding.rvChildList.adapter = list?.let { ListChildesAdapter(this::onItemClick, it) }
    }

    private fun onItemClick(child: Child) {
        startActivity(Intent(this, MainActivityChild::class.java).apply {
            putExtra("child", Gson().toJson(child))
        })
    }
}