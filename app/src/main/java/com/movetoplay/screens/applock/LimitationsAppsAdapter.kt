package com.movetoplay.screens.applock

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R
import com.movetoplay.domain.model.user_apps.UserApp

class LimitationsAppsAdapter(private val list: List<UserApp>) : RecyclerView.Adapter<LimitationsAppsAdapter.ViewHolder>() {
    private val blockedList = HashMap<String, String>()
    var context1: Context? = null
    init {
        list.forEach {
            if (it.type == "unlimited") {
                blockedList[it.id] = (it.type)
            }
        }
        Log.e("adapter", "block apps: $blockedList")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.limitations_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Log.e("limit", "onBindViewHolder: ${list[position]}")
        holder.onBind(list[position])
        val apkInfoExtractor = ApkInfoExtractor(context1)

        val ApplicationPackageName = list.get(position) as String
        val drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName)
        holder.image.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var title = itemView.findViewById<TextView>(R.id.tv_limitations)
        internal var image = itemView.findViewById<ImageView>(R.id.img_app_icon)
        private var status = itemView.findViewById<ToggleButton>(R.id.toggle_button)

        fun onBind(app: UserApp) {
            title.text = app.name

            if (blockedList[app.id]?.isNotEmpty() == true) {
                if (blockedList[app.id].equals("unlimited")) {
                    status.isChecked = true
                }
            }

            status.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    blockedList[app.id] = "unlimited"
                } else {
                    blockedList[app.id] = "limited"
                }
            }
        }
    }

    fun getBlockedApps(): HashMap<String, String> {
        Log.e("adapter", "block apps: $blockedList")
        return blockedList
    }
}
