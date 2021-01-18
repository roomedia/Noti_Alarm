package com.roomedia.dawn_down_alarm.ui.main.show

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.ui.main.KeywordViewModel
import kotlinx.android.synthetic.main.fragment_show.*

class ShowKeywordsFragment : Fragment() {

    private lateinit var keywordViewModel: KeywordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keywordViewModel = ViewModelProvider(this).get(KeywordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        keywordViewModel.getAll().observe(
            viewLifecycleOwner,
            Observer { keywords ->
                appsRecyclerView.adapter = ShowAdapter(keywords, keywordViewModel)
            }
        )
    }
}