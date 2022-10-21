package com.movetoplay.screens.applock

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.Pref

class LimitationsAppsAdapter(
    private val list: ArrayList<UserApp>,
    private val listener: (UserApp) -> Unit
) :
    RecyclerView.Adapter<LimitationsAppsAdapter.ViewHolder>() {

    private val blockedList = HashSet<String>()

    init {
        list.forEach {
            if (it.type == "unlimited")
                blockedList.add(it.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.limitations_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("limit", "onBindViewHolder: ${list[position]}")
        holder.onBind(list[position])
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

            blockedList.forEach {
                if (it == app.id)
                    status.isChecked = true
            }

            status.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    blockedList.add(app.id)
                } else {
                    blockedList.remove(app.id)
                }
            }
        }
    }

    fun getBlockedApps(): HashSet<String> {
        return blockedList
    }

}