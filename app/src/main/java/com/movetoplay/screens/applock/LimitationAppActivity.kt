package com.movetoplay.screens.applock

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
           getAllApps()

        findViewById<RecyclerView>(R.id.rv_limitations).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@LimitationAppActivity.adapter
        }
        adapter.notifyDataSetChanged()

    }

      @Throws(PackageManager.NameNotFoundException::class)
    fun getAllApps() {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val ril = packageManager.queryIntentActivities(mainIntent, 0)
        var name: String?
        var i = 0

        val apps = arrayOfNulls<String>(ril.size)
        for (ri in ril) {
            if (ri.activityInfo != null) {
                val res: Resources =
                    packageManager.getResourcesForApplication(ri.activityInfo.applicationInfo)
                name = if (ri.activityInfo.labelRes != 0) {
                    res.getString(ri.activityInfo.labelRes)
                } else {
                    ri.activityInfo.applicationInfo.loadLabel(
                        packageManager
                    ).toString()
                }
                apps[i] = name
                i++
            }
        }
        adapter = LimitationsAppsAdapter(apps)
    }
}
