package com.roomedia.dawn_down_alarm.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.databinding.FragmentEditKeywordBinding
import com.roomedia.dawn_down_alarm.entity.App

class EditKeywordBottomSheetDialogFragment(private val target: App) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditKeywordBinding? = null
    private val binding get() = _binding!!

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
            app = target
            startTime.setIs24HourView(true)
            endTime.setIs24HourView(true)
            keywordInput.requestFocus()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}