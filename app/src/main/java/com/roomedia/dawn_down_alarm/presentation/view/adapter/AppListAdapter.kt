package com.roomedia.dawn_down_alarm.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roomedia.dawn_down_alarm.databinding.ItemAppListBinding
import com.roomedia.dawn_down_alarm.entity.AppAndKeywords
import com.roomedia.dawn_down_alarm.presentation.view.fragment.AppListFragment
import com.roomedia.dawn_down_alarm.presentation.view.fragment.EditKeywordBottomSheetDialogFragment

class AppListAdapter(private val fragment: AppListFragment) : ListAdapter<AppAndKeywords, AppListAdapter.ViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun showBottomSheetDialog(view: View) {
        EditKeywordBottomSheetDialogFragment(getItem(view.id)) {
            notifyItemChanged(view.id)
        }.show(fragment.childFragmentManager, null)
    }

    inner class ViewHolder(private val binding: ItemAppListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appAndKeywords: AppAndKeywords, position: Int) {
            binding.adapter = this@AppListAdapter
            binding.viewModel = fragment.appListViewModel

            binding.app = appAndKeywords.app
            binding.keywords = appAndKeywords.keywords
            binding.root.id = position
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<AppAndKeywords>() {
        override fun areItemsTheSame(oldItem: AppAndKeywords, newItem: AppAndKeywords): Boolean {
            return oldItem.app?.packageName == newItem.app?.packageName
        }

        override fun areContentsTheSame(oldItem: AppAndKeywords, newItem: AppAndKeywords): Boolean {
            return oldItem.app == newItem.app
        }
    }
}