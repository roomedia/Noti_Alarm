package com.roomedia.dawn_down_alarm.ui.main.show

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.model.Keyword
import com.roomedia.dawn_down_alarm.ui.main.KeywordViewModel
import kotlinx.android.synthetic.main.recycler_show_item.view.*

class ShowAdapter(private val dataset: List<Keyword>, private val keywordViewModel: KeywordViewModel):
    RecyclerView.Adapter<ShowAdapter.KeywordViewHolder>() {

    class KeywordViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_show_item, parent, false)
        return KeywordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        val it = holder.view
        it.context.packageManager.run {
            it.iconImageView.setImageDrawable(getApplicationIcon(dataset[position].packageName))
        }
        it.nameTextView.text = dataset[position].name
        it.keywordTextView.text = dataset[position].keyword

        it.keywordDeleteButton.setOnClickListener {
            keywordViewModel.delete(dataset[position])
        }
    }

    override fun getItemCount() = dataset.size
}