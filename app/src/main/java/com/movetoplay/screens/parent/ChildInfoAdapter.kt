package com.movetoplay.screens.parent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.util.load

class ChildInfoAdapter(
    private val list: ArrayList<UserApp>
) :
    RecyclerView.Adapter<ChildInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_child_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var title = itemView.findViewById<TextView>(R.id.tv_child_info_app)
        internal var image = itemView.findViewById<ImageView>(R.id.img_child_info)
        private var usedTime = itemView.findViewById<TextView>(R.id.tv_used_time)

        fun onBind(app: UserApp) {
            title.text = app.name
            usedTime.text = itemView.context.getString(R.string.min_day, (app.time ?: 0).toString())
            app.drawable?.let { image.load(it) }
        }
    }
}