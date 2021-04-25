package com.roomedia.dawn_down_alarm.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.roomedia.dawn_down_alarm.ui.main.add.AddKeywordsFragment
import com.roomedia.dawn_down_alarm.ui.main.show.ShowKeywordsFragment

class SectionsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 2

    @Throws(java.lang.IndexOutOfBoundsException::class)
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddKeywordsFragment()
            1 -> ShowKeywordsFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }
}