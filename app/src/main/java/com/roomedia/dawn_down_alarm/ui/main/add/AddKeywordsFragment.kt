package com.roomedia.dawn_down_alarm.ui.main.add

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.model.Keyword
import com.roomedia.dawn_down_alarm.ui.main.KeywordViewModel
import kotlinx.android.synthetic.main.fragment_add.*

class AddKeywordsFragment : Fragment() {

    private val keywordViewModel by lazy {
        ViewModelProvider(this).get(KeywordViewModel::class.java)
    }
    private val viewAdapter by lazy {
        val appList = getAppsInfo(context!!)
        AddAdapter(appList)
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
            adapter = viewAdapter
            (layoutManager as GridLayoutManager).spanCount =
                if (resources.configuration.orientation == ORIENTATION_PORTRAIT) 4 else 7
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
            keywordViewModel.insert(Keyword(this.toString(), selected.first, selected.second))
            textInput.setText("")
        }
        return true
    }

    private fun getAppsInfo(context: Context): List<ActivityInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        return context.packageManager.queryIntentActivities(mainIntent, 0)
            .map { it.activityInfo }
            .sortedBy { it.applicationInfo.loadLabel(context.packageManager).toString() }
    }
}