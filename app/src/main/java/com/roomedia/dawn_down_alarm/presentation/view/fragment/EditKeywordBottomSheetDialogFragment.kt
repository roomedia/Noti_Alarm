package com.roomedia.dawn_down_alarm.presentation.view.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.data.AppListViewModel
import com.roomedia.dawn_down_alarm.data.CommonViewModelFactory
import com.roomedia.dawn_down_alarm.data.KeywordViewModel
import com.roomedia.dawn_down_alarm.databinding.FragmentEditKeywordBinding
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.entity.Keyword
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import com.roomedia.dawn_down_alarm.util.getTimeValue
import io.reactivex.rxjava3.schedulers.Schedulers

class EditKeywordBottomSheetDialogFragment(private val packageName: String, private val onDestroyCallback: () -> Unit) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditKeywordBinding? = null
    private val binding get() = _binding!!

    private val appListViewModel: AppListViewModel by viewModels({ requireParentFragment() })
    private val keywordViewModel: KeywordViewModel by viewModels {
        CommonViewModelFactory(AlarmApplication.instance.keywordDao)
    }

    private lateinit var oldApp: App
    private lateinit var oldKeywords: Set<Keyword>
    private lateinit var newKeywords: MutableSet<Keyword>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_PopupOverlay)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditKeywordBinding.inflate(inflater, container, false).apply {
            startTime.setIs24HourView(true)
            endTime.setIs24HourView(true)
            keywordInput.requestFocus()
            keywordInput.setOnEditorActionListener { _, actionId, _ ->
                when {
                    actionId != EditorInfo.IME_ACTION_DONE -> false
                    else -> addKeyword()
                }
            }
            keywordInput.setOnKeyListener { _, keyCode, keyEvent ->
                when {
                    keyEvent.action != KeyEvent.ACTION_DOWN -> false
                    keyCode != KeyEvent.KEYCODE_ENTER && keyCode != KeyEvent.KEYCODE_NUMPAD_ENTER -> false
                    else -> addKeyword()
                }
            }
        }
        appListViewModel.get(packageName).subscribeOn(Schedulers.io())
            .subscribe { (app, keywords) ->
                binding.app = app
                keywords.forEach { addChip(it.keyword) }

                oldApp = app!!.copy()
                oldKeywords = keywords
                newKeywords = keywords.toMutableSet()
            }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val newApp = binding.app!!.apply {
            isEnabled = binding.enableSwitch.isChecked
            startTime = binding.startTime.getTimeValue()
            endTime = binding.endTime.getTimeValue()
        }
        if (oldApp != newApp || newKeywords != oldKeywords) {
            appListViewModel.update(newApp)
            keywordViewModel.replace(packageName, newKeywords)
        }
        onDestroyCallback()
        _binding = null
    }

    private fun addKeyword(): Boolean {
        val keyword = Keyword(binding.keywordInput.text.toString(), packageName)
        when {
            binding.keywordInput.text.isNullOrEmpty() -> Toast.makeText(context, R.string.hint, Toast.LENGTH_SHORT).show()
            newKeywords.contains(keyword) -> {}
            else -> {
                addChip(keyword.keyword)
                newKeywords.add(keyword)
            }
        }
        binding.keywordInput.setText("")
        return true
    }

    private fun addChip(keyword: String) {
        val chip = Chip(context).apply {
            text = keyword
            isCloseIconVisible = true
            setOnCloseIconClickListener { chip ->
                newKeywords.removeAll { it.keyword == text }
                binding.keywordChipGroup.removeView(chip)
            }
        }
        binding.keywordChipGroup.addView(chip)
    }
}