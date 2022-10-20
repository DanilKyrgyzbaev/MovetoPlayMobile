package com.movetoplay.screens.create_child_profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R
import com.movetoplay.domain.model.Child
import com.movetoplay.util.visible


class ListChildesAdapter(private val listener: (Child) -> Unit, private val list: List<Child>) :
    RecyclerView.Adapter<ListChildesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_childes_item, parent, false)
        return ViewHolder(view)
    }
    private var id = ""
    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.title.text = list[position].fullName
        if (id==list[position].id) {
            holder.ivSelected.visible(!holder.ivSelected.isVisible)
        }else  holder.ivSelected.visible(false)

        holder.itemView.setOnClickListener {
            listener(list[position])
            id = list[position].id
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title = itemView.findViewById<TextView>(R.id.tv_childes_item)
        internal var ivSelected = itemView.findViewById<AppCompatImageView>(R.id.iv_childes_item)
    }
}