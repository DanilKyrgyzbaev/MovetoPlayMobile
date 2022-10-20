package com.movetoplay.screens.create_child_profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.movetoplay.R
import com.movetoplay.databinding.ActivitySetupProfileBinding
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.Gender
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.screens.MainActivity
import com.movetoplay.screens.parent.MainActivityParent
import com.movetoplay.util.inVisible
import com.movetoplay.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupProfileBinding
    private val viewModel: SetupProfileViewModel by viewModels()
    private var statusArr = arrayOf("Создать новый", "Импортировать старый")
    private var genderArr = arrayOf("Мужской", "Женский")
    private var gender: Gender = Gender.MAN
    private lateinit var childesAdapter: ListChildesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner()
        initListeners()
    }

    private fun spinner() {

        val statusArr = ArrayAdapter(this, R.layout.spin_status_item, statusArr)
        binding.spStatus.adapter = statusArr
        val genderArr = ArrayAdapter(this, R.layout.spin_gender_item, genderArr)
        binding.spChildGender.adapter = genderArr

        binding.spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (position == 1) {
                    hideViews()
                    viewModel.getChildesList()
                } else showViews()

                binding.btnContinue.isClickable = true
                statusArr.getPosition(id.toString())
                Pref.either_new_or_old = statusArr.getItem(position).toString()
                Log.e("statusArr", statusArr.getItem(position).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        binding.spChildGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                gender = if (position == 1)
                    Gender.WOMAN
                else Gender.MAN
                genderArr.getPosition(id.toString())
                Pref.gender = genderArr.getPosition(id.toString()).toString()
                Log.e("genderArr", genderArr.getItem(position).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
    }

    private fun showViews() {
        binding.apply {
            rvChildList.visible(false)
            spChildGender.inVisible(false)
            etChildAge.inVisible(false)
            etChildName.inVisible(false)
            checkSport.inVisible(false)
        }
    }

    private fun hideViews() {
        binding.apply {
            rvChildList.visible(true)
            spChildGender.inVisible(true)
            etChildAge.inVisible(true)
            etChildName.inVisible(true)
            checkSport.inVisible(true)
        }
        Toast.makeText(this, "Выберите профиль ребёнка", Toast.LENGTH_SHORT).show()
    }

    private fun initListeners() {
        binding.apply {
            btnContinue.setOnClickListener {
                when (Pref.either_new_or_old) {
                    statusArr[0] -> {
                        viewModel.sendProfileChild(
                            etChildName.text.toString(),
                            etChildAge.text.toString(),
                            gender,
                            checkSport.isChecked,
                            this@SetupProfileActivity
                        )
                    }
                    statusArr[1] -> {
                        Log.e("TAG", "initListeners: get")
                        if (Pref.childId != "") {
                            Toast.makeText(
                                this@SetupProfileActivity,
                                "Профиль успешно импортирован!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            chooseGoToActivity()
                        }
                    }
                }
            }
        }

        viewModel.createResultStatus.observe(this) {
            binding.pbProfile.apply {
                when (it) {
                    is ResultStatus.Loading -> {
                        Log.e("TAG", "CreateProfile loading: ")
                        binding.btnContinue.isClickable = false
                        visible(true)
                    }
                    is ResultStatus.Success -> {
                        Log.e("TAG", "createProfile SUCCESS: ${it.data}")
                        Pref.childId = it.data?.id.toString()
                        Pref.parentId = it.data?.parentAccountId.toString()
                        Toast.makeText(
                            this@SetupProfileActivity,
                            "Профиль успешно создан!",
                            Toast.LENGTH_SHORT
                        ).show()
                        visible(false)
                        chooseGoToActivity()
                    }
                    is ResultStatus.Error -> {
                        visible(false)
                        binding.btnContinue.isClickable = true

                        Log.e("TAG", "createProfile ERROR: ${it.error}")
                        Toast.makeText(this@SetupProfileActivity, it.error, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewModel.getChildesResultStatus.observe(this) {
            binding.pbProfile.apply {
                when (it) {
                    is ResultStatus.Loading -> {
                        binding.btnContinue.isClickable = false
                        visible(true)
                    }
                    is ResultStatus.Success -> {
                        visible(false)
                        Log.e("TAG", "getChildesResultStatus: ${it.data} ")
                        it.data?.let { it1 -> setChildesData(it1) }
                    }
                    is ResultStatus.Error -> {
                        visible(false)
                        Toast.makeText(this@SetupProfileActivity, it.error, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        viewModel.syncProfileStatus.observe(this){
            binding.pbProfile.apply {
                when (it) {
                    is ResultStatus.Loading -> {
                        binding.btnContinue.isClickable = false
                        visible(true)
                        Toast.makeText(this@SetupProfileActivity, "Синхронизация данных...", Toast.LENGTH_SHORT).show()
                    }
                    is ResultStatus.Success -> {
                        visible(false)
                        Log.e("TAG", "Sync Result Status Success: ${it.data} ")
                        goToMainActivity()
                    }
                    is ResultStatus.Error -> {
                        visible(false)
                        Log.e("TAG", "Sync Result Status Error: ${it.error} ")
                        goToMainActivity()
                    }
                }
            }
        }
    }

    private fun chooseGoToActivity() {
        if (Pref.isChild) {
           viewModel.syncProfile()
        } else startActivity(Intent(this, MainActivityParent::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
        finishAffinity()
    }

    private fun goToMainActivity(){
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
    }

    private fun setChildesData(list: List<Child>) {
        if (list.isEmpty()) Toast.makeText(
            this@SetupProfileActivity,
            "У вас нет профилей ребенка, создайте новый",
            Toast.LENGTH_SHORT
        ).show()
        else {
            childesAdapter = ListChildesAdapter(this::onChildesItemClick, list)
            binding.rvChildList.adapter = childesAdapter
        }
    }

    private fun onChildesItemClick(child: Child) {
        Pref.childId = child.id
        Pref.parentId = child.parentAccountId
    }
}