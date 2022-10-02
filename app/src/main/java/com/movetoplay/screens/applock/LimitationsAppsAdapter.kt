package com.movetoplay.screens.applock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R

class LimitationsAppsAdapter(private var listOfApps: Array<String?>) :
    RecyclerView.Adapter<LimitationsAppsAdapter.ViewHolder>() {
//    private val appsList: List<AppsModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.limitations_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOfApps[position])
    }

    override fun getItemCount(): Int {
        return listOfApps.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var title = itemView.findViewById<TextView>(R.id.tv_limitations)/*
        private var image = itemView.findViewById<ImageView>(R.id.img_app_icon)
        private var status = itemView.findViewById<ToggleButton>(R.id.toggle_button)*/

        fun bind(get: String?) {
            title.text = get
        }
    }
}
