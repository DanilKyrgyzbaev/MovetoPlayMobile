package com.movetoplay.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R

class LimitationAppActivity : AppCompatActivity() {

    private lateinit var adapter: LimitationsAppsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limitation_app)

        initViews()
    }

    private fun initViews() {
        val test = arrayListOf<String>()
        test.addAll(listOf("Instagram", "WhatsApp", "VK", "YouTube"))

        adapter = LimitationsAppsAdapter(test)

        findViewById<RecyclerView>(R.id.rv_limitations).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@LimitationAppActivity.adapter
        }
        adapter.notifyDataSetChanged()
    }

}