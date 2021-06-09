package com.roomedia.dawn_down_alarm.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.roomedia.dawn_down_alarm.databinding.RecyclerAppListItemBinding
import com.roomedia.dawn_down_alarm.entity.App
import java.util.*

class AppListAdapter : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    private var _dataset = mutableListOf<App>()
    private val dataset get() = _dataset.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerAppListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = dataset[position]
        val packageManager = holder.binding.root.context.packageManager

        holder.binding.apply {
            iconImageView.setImageDrawable(packageManager.getApplicationIcon(app.packageName))
            nameTextView.text = app.appName
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun replaceItems(items: List<App>) {
        val diffResult = DiffUtil.calculateDiff(Callback(_dataset, items))
        _dataset.clear()
        _dataset.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: RecyclerAppListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    inner class Callback(private val old: List<App>, private val new: List<App>) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].packageName == new[newItemPosition].packageName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]
            return oldItem.appName == newItem.appName && oldItem.icon == newItem.icon
        }
    }
}