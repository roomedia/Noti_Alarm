package com.roomedia.dawn_down_alarm.ui.main.add

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roomedia.dawn_down_alarm.R
import kotlinx.android.synthetic.main.recycler_add_item.view.*

class AddAdapter(private val dataset: List<ActivityInfo>):
    RecyclerView.Adapter<AddAdapter.AppViewHolder>() {

    var selected: Pair<String, String>? = null
    private var selectedPosition = -1
    class AppViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_add_item, parent, false)
        return AppViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val name: CharSequence
        val packageName: String

        with (holder.view) {
            context.packageManager.apply {
                packageName = dataset[position].packageName
                name = getApplicationLabel(dataset[position].applicationInfo)
            }.run {
                iconImageView.setImageDrawable(getApplicationIcon(packageName))
                nameTextView.text = name
            }

            if (selectedPosition == position) {
                setBackgroundResource(R.color.colorPrimaryDark)
                nameTextView.setTextColor(Color.rgb(230, 230, 230))
            } else {
                setBackgroundResource(android.R.color.transparent)
                nameTextView.setTextColor(Color.rgb(128, 128, 128))
            }

            setOnClickListener {
                selected = Pair(packageName, name.toString())
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = dataset.size
}