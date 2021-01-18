package com.roomedia.dawn_down_alarm.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.roomedia.dawn_down_alarm.ui.main.add.AddKeywordsFragment
import com.roomedia.dawn_down_alarm.ui.main.show.ShowKeywordsFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddKeywordsFragment()
            1 -> ShowKeywordsFragment()
            else -> Fragment()
        }
    }
}