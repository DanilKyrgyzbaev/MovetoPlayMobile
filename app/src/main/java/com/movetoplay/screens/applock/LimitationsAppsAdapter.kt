package com.movetoplay.screens.applock

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.util.load

class LimitationsAppsAdapter(
    list: ArrayList<UserApp>
) :
    RecyclerView.Adapter<LimitationsAppsAdapter.ViewHolder>() {
    private var list: ArrayList<UserApp>

    init {
        this.list = list
    }

    fun updateList(list: ArrayList<UserApp>) {
        this.list = list
        notifyDataSetChanged()
    }

    private val blockedList = HashMap<String, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.limitations_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("limit", "adapter position:  $position")
        Log.e("limit", "app: ${list[position].name}")
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
            status.isChecked = app.type == "unallowed"

            app.drawable?.let { image.load(it) }

            status.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    blockedList[app.id] = "unallowed"
                } else {
                   blockedList[app.id] = "allowed"
                }
            }
        }
    }

    fun getBlockedApps(): HashMap<String, String> {
        Log.e("adapter", "block apps: $blockedList")
        return blockedList
    }

}