package com.roo_media_.noti_alarm.ui.main.add

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.roo_media_.noti_alarm.R
import com.roo_media_.noti_alarm.model.Keyword
import com.roo_media_.noti_alarm.ui.main.KeywordViewModel
import kotlinx.android.synthetic.main.fragment_add.*

class AddKeywordsFragment : Fragment() {

    private lateinit var keywordViewModel: KeywordViewModel
    private lateinit var appList: List<ActivityInfo>
    private lateinit var viewAdapter: AddAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keywordViewModel = ViewModelProvider(this).get(KeywordViewModel::class.java)
        appList = getAppsInfo(context!!)
        viewAdapter = AddAdapter(appList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // update UI
        appsGridView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 4)
            adapter = viewAdapter
        }

        // bind action
        keywordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) { return@setOnEditorActionListener false }
            addKeyword(keywordInput)
        }

        addButton.setOnClickListener {
            addKeyword(keywordInput)
        }
    }

    private fun addKeyword(textInput: TextInputEditText): Boolean {
        textInput.text.apply {
            if (isNullOrBlank() || isNullOrEmpty()) {
                Toast.makeText(context, R.string.request, Toast.LENGTH_SHORT).show()
                return@addKeyword false
            }

            // do db thing.
            val selected = viewAdapter.selected ?: return false
            keywordViewModel.insert(Keyword(0, this.toString(), selected.first, selected.second))
            textInput.setText("")
        }
        return true
    }

    private fun getAppsInfo(context: Context): List<ActivityInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        return context.packageManager
            .queryIntentActivities(mainIntent, 0)
            .map {
                it.activityInfo
            }
    }
}