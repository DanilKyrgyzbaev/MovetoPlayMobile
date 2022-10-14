package com.movetoplay.screens.create_child_profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.movetoplay.R
import com.movetoplay.databinding.ActivitySetupProfileBinding
import com.movetoplay.databinding.SpinStatusItemBinding
import com.movetoplay.model.CreateProfile
import com.movetoplay.pref.Pref
import com.movetoplay.screens.MainActivity
import com.movetoplay.screens.register.RegisterViewModel

class SetupProfileActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySetupProfileBinding
    lateinit var viewModel: SetupProfileViewModel

    private var statusArr = arrayOf("Создать новый", "Импортировать старый")
    private var genderArr = arrayOf("Мужской","Женский")
    private var childName: String? = null
    private var childAge: String? = null
    private var child_play_sportsBox: Boolean = false
    //val languages = resources.getStringArray(R.array.Languages)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SetupProfileViewModel::class.java]

        spinner()
        initListeners()
    }

    private fun spinner(){

        val statusArr = ArrayAdapter(this, R.layout.spin_status_item, statusArr)
        binding.spStatus.adapter = statusArr

        val genderArr = ArrayAdapter(this, R.layout.spin_gender_item, genderArr)
        binding.spChildGender.adapter = genderArr

        binding.spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               statusArr.getPosition(id.toString())
                Pref.either_new_or_old = statusArr.getPosition(id.toString()).toString()

                Log.e("statusArr",statusArr.getItem(position).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.spChildGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genderArr.getPosition(id.toString())
                Pref.gender = genderArr.getPosition(id.toString()).toString()
                Log.e("genderArr", genderArr.getItem(position).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun initListeners() {
        childName = binding.etChildName.text.toString()
        childAge = binding.etChildAge.text.toString()
        binding.btnContinue.setOnClickListener(View.OnClickListener { view: View? ->
            if (childName!!.isBlank()||childAge!!.isBlank()){
                if (binding.checkSport.isChecked){
                    child_play_sportsBox = true
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                } else{
                    child_play_sportsBox = false
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
                Pref.playingSports = child_play_sportsBox
                Pref.childName = binding.etChildName.text.toString()
                Pref.childAge = binding.etChildAge.text.toString()
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
            viewModel.sendProfileChild(Pref.userToken, CreateProfile(Pref.childName, Pref.childAge.toInt(),Pref.gender,Pref.playingSports),this)
        })
    }
}