package com.roo_media_.noti_alarm

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.roo_media_.noti_alarm.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_grant.*
import kotlinx.android.synthetic.main.activity_main.*

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class MainActivity : AppCompatActivity() {

    private var past: Boolean? = null
    private var now: Boolean = false

    override fun onResume() {
        super.onResume()
        now = permissionGranted()
        if (past != null && past == now)
            return

        past = now
        setView(now)
    }

    private fun setView(granted: Boolean) {
        if (!granted) {
            setContentView(R.layout.activity_grant)
            grantButton.setOnClickListener {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)
            }
            return
        }

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, lifecycle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setText(TAB_TITLES[position])
        }.attach()
    }

    private fun permissionGranted(): Boolean {
        val sets = NotificationManagerCompat.getEnabledListenerPackages(this)
        return sets.contains(packageName)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_actions, menu)

        val pref = getSharedPreferences("enabled", Context.MODE_PRIVATE)
        val switch = menu?.findItem(R.id.alarmSwitch)?.actionView?.findViewById<Switch>(R.id.alarmSwitch)

        switch?.isChecked = pref.getBoolean("enabled", false)
        switch?.setOnClickListener {
            pref.edit().putBoolean("enabled", switch.isChecked).apply()
        }
        return true
    }
}