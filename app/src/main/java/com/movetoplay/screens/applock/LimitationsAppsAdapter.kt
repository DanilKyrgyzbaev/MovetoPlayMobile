package com.movetoplay.screens.applock

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.movetoplay.R


class LimitationsAppsAdapter(private val context: Context, private val list: List<String?>, private val blackListApps: HashSet<String>) :
    RecyclerView.Adapter<LimitationsAppsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.limitations_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apkInfoExtractor = ApkInfoExtractor(context)

        val applicationPackageName = list[position]
        val applicationLabelName: String = apkInfoExtractor.GetAppName(applicationPackageName)
        val drawable: Drawable = apkInfoExtractor.getAppIconByPackageName(applicationPackageName)

        holder.title.text = applicationLabelName
        holder.image.setImageDrawable(drawable)

//        val i: Iterator<String> = blackListApps.iterator()
//        while (i.hasNext()) {
//            if (i.next() == applicationPackageName){
//                holder.status.isChecked = true
//
//            }
//        }
           blackListApps.forEach {
            if (it==applicationPackageName){
                holder.status.isChecked = true

            }
        }

        holder.status.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (applicationPackageName != null) {
                    blackListApps.add(applicationPackageName)
                }
                Toast.makeText(context, "Added to black list", Toast.LENGTH_SHORT).show()
                Log.e("Toggle", "onBindViewHolder: $blackListApps")
            } else {
                blackListApps.remove(applicationPackageName)
            }
        }
    }

    fun getBlackListApps(): HashSet<String> {
        return blackListApps
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title = itemView.findViewById<TextView>(R.id.tv_limitations)
        internal var image = itemView.findViewById<ImageView>(R.id.img_app_icon)
        internal var status = itemView.findViewById<ToggleButton>(R.id.toggle_button)
    }
}