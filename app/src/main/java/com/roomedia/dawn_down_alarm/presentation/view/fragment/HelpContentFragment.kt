package com.roomedia.dawn_down_alarm.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.databinding.FragmentHelpContentBinding
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication

class HelpContentFragment(private val position: Int) : Fragment() {

    private var _binding: FragmentHelpContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpContentBinding.inflate(inflater, container, false).apply {
            contentImageView.setImageDrawable(HELP_IMAGE_ARRAY.getDrawable(position))
            titleTextView.text = HELP_TITLE_ARRAY[position]
            bodyTextView.text = HELP_BODY_ARRAY[position]
            extraFunctionButton.text = HELP_BUTTON_ARRAY[position]
            extraFunctionButton.setOnClickListener {
                val action = when (position) {
                    0 -> "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
                    1 -> "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS"
                    else -> ""
                }
                startActivity(Intent(action))
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val HELP_IMAGE_ARRAY = AlarmApplication.instance.resources.obtainTypedArray(R.array.help_image)
        private val HELP_TITLE_ARRAY = AlarmApplication.instance.resources.getStringArray(R.array.help_title)
        private val HELP_BODY_ARRAY = AlarmApplication.instance.resources.getStringArray(R.array.help_body)
        private val HELP_BUTTON_ARRAY = AlarmApplication.instance.resources.getStringArray(R.array.help_button)
    }
}