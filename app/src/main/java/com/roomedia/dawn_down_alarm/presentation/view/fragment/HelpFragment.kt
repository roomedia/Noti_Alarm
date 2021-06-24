package com.roomedia.dawn_down_alarm.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false).also {
            it.viewPager2.adapter = ScreenSlidePagerAdapter(this)
            it.nextButton.setOnClickListener { _ ->
                it.viewPager2.currentItem = (it.viewPager2.currentItem + 1) % NUM_PAGES
            }
            TabLayoutMediator(it.tabLayout, it.viewPager2) { _, _ -> }.attach()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = NUM_PAGES
        override fun createFragment(position: Int) = HelpContentFragment(position)
    }

    companion object {
        private const val NUM_PAGES = 2
    }
}