package com.roomedia.dawn_down_alarm.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.roomedia.dawn_down_alarm.data.AppListViewModel
import com.roomedia.dawn_down_alarm.databinding.ItemAppListBinding
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.entity.Keyword
import com.roomedia.dawn_down_alarm.presentation.view.fragment.EditKeywordBottomSheetDialogFragment
import java.lang.ref.WeakReference

class AppListAdapter(private val fragmentManagerRef: WeakReference<FragmentManager>) : ListAdapter<App, AppListAdapter.ViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun showBottomSheetDialog(view: View) {
        fragmentManagerRef.get()?.let { fragmentManager ->
            EditKeywordBottomSheetDialogFragment(getItem(view.id)).show(fragmentManager, null)
        }
    }

    inner class ViewHolder(private val binding: ItemAppListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: App, position: Int) {
            binding.adapter = this@AppListAdapter
            binding.app = app
            // TODO: change dummy keywords to room entity
            app.keywords = listOf(
                Keyword(app.packageName, "pack1", "name1"),
                Keyword(app.appName, "pack2", "name2"),
                Keyword(app.endTime.toString(), "pack3", "name3"),
            )
            binding.root.id = position
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<App>() {
        override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem == newItem
        }
    }
}