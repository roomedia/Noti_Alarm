package com.roo_media_.noti_alarm.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.roo_media_.noti_alarm.R
import com.roo_media_.noti_alarm.ui.main.add.AddKeywordsFragment
import com.roo_media_.noti_alarm.ui.main.show.ShowKeywordsFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fm, lifecycle) {

//    override fun getPageTitle(position: Int): CharSequence? {
//        return context.resources.getString(TAB_TITLES[position])
//    }

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