package com.roomedia.dawn_down_alarm.presentation.view.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.data.CommonViewModelFactory
import com.roomedia.dawn_down_alarm.data.KeywordViewModel
import com.roomedia.dawn_down_alarm.databinding.ItemAppListBinding
import com.roomedia.dawn_down_alarm.entity.AppAndKeywords
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import com.roomedia.dawn_down_alarm.presentation.view.fragment.AppListFragment
import com.roomedia.dawn_down_alarm.presentation.view.fragment.EditKeywordBottomSheetDialogFragment
import io.github.bangjunyoung.KoreanTextMatcher
import java.util.*

class AppListAdapter(private val fragment: AppListFragment) : ListAdapter<AppAndKeywords, AppListAdapter.ViewHolder>(DiffUtilCallback) {

    private var totalList: List<AppAndKeywords>? = emptyList()
    private var query: String? = null
    private var enabledFilter = false
    private var hasKeywordsFilter = false

    private val appListViewModel = fragment.appListViewModel
    private val keywordViewModel: KeywordViewModel by fragment.viewModels {
        CommonViewModelFactory(AlarmApplication.instance.keywordDao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun submitList(list: List<AppAndKeywords>?) {
        var filteredList = totalList
        if (query != null) {
            val query = query!!.toLowerCase(Locale.ROOT)
            val matcher = KoreanTextMatcher(query)
            filteredList = filteredList?.filter {
                val appName = it.app?.appName?.toLowerCase(Locale.ROOT)
                val keywords = it.keywords.joinToString(separator = "\n").toLowerCase(Locale.ROOT)
                appName?.contains(query) == true
                        || keywords.contains(query)
                        || matcher.match(appName).success()
                        || matcher.match(keywords).success()
            }
        }
        if (enabledFilter) {
            filteredList = filteredList?.filter { it.app?.isEnabled == true }
        }
        if (hasKeywordsFilter) {
            filteredList = filteredList?.filter { it.keywords.isNotEmpty() }
        }
        super.submitList(filteredList)
    }

    fun updateList(list: List<AppAndKeywords>?) {
        totalList = list
        submitList(list)
    }

    fun updateQuery(query: String?) {
        this.query = query
        submitList(totalList)
    }

    fun updateFilter(enabledFilter: Boolean = this.enabledFilter, hasKeywordsFilter: Boolean = this.hasKeywordsFilter) {
        this.enabledFilter = enabledFilter
        this.hasKeywordsFilter = hasKeywordsFilter
        submitList(totalList)
    }

    fun showBottomSheetDialog(view: View) {
        EditKeywordBottomSheetDialogFragment(getItem(view.id)) {
            notifyItemChanged(view.id)
        }.show(fragment.childFragmentManager, null)
    }

    inner class ViewHolder(private val binding: ItemAppListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appAndKeywords: AppAndKeywords, position: Int) {
            binding.adapter = this@AppListAdapter
            binding.viewModel = appListViewModel

            val (app, keywords) = appAndKeywords
            binding.app = app!!
            binding.keywords = keywords
            binding.root.id = position

            binding.root.setOnLongClickListener {
                val context = binding.root.context
                val isEnabled = if (app.isEnabled) R.string.disable else R.string.enable
                val array = arrayOf(
                    context.getString(R.string.able_app, context.getString(isEnabled)),
                    context.getString(R.string.delete_all_keywords),
                )
                AlertDialog.Builder(binding.root.context).setItems(array) { _, index ->
                    when (index) {
                        0 -> {
                            appListViewModel.updateEnabled(app)
                            notifyItemChanged(position)
                        }
                        1 -> keywordViewModel.delete(app.packageName)
                    }
                }.create().show()
                true
            }
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<AppAndKeywords>() {
        override fun areItemsTheSame(oldItem: AppAndKeywords, newItem: AppAndKeywords): Boolean {
            return oldItem.app?.packageName == newItem.app?.packageName
        }

        override fun areContentsTheSame(oldItem: AppAndKeywords, newItem: AppAndKeywords): Boolean {
            return oldItem.app == newItem.app && oldItem.keywords == newItem.keywords
        }
    }
}