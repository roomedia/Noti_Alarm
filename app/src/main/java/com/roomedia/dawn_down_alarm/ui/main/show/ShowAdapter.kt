package com.roomedia.dawn_down_alarm.ui.main.show

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.model.Keyword
import kotlinx.android.synthetic.main.recycler_show_item.view.*

class ShowAdapter(private val deleteCallback: (Keyword) -> Unit) :
    RecyclerView.Adapter<ShowAdapter.KeywordViewHolder>() {

    private val dataset: MutableList<Keyword> = mutableListOf()

    fun updateDataset(keywords: List<Keyword>) {
        dataset.clear()
        dataset.addAll(keywords)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_show_item, parent, false)
        return KeywordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        with (holder.view) {
            context.packageManager.run {
                iconImageView.setImageDrawable(getApplicationIcon(dataset[position].packageName))
            }
            nameTextView.text = dataset[position].name
            keywordTextView.text = dataset[position].keyword

            keywordDeleteButton.setOnClickListener {
                deleteCallback(dataset[position])
            }
        }
    }

    override fun getItemCount() = dataset.size

    inner class KeywordViewHolder(val view: View): RecyclerView.ViewHolder(view)
}