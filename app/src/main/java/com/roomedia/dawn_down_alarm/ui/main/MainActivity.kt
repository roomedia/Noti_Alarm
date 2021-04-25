package com.roomedia.dawn_down_alarm.ui.main

import android.content.Context
import android.content.Intent
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.databinding.ActivityGrantBinding
import com.roomedia.dawn_down_alarm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_grant.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.switch_item.view.*

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class MainActivity : AppCompatActivity() {

    private var pastPermissionGranted: Boolean? = null
    private var currentPermissionGranted: Boolean = false

    private val activityGrantBinding by lazy { ActivityGrantBinding.inflate(layoutInflater) }
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onResume() {
        super.onResume()
        currentPermissionGranted = permissionGranted()
        if (pastPermissionGranted != null && pastPermissionGranted == currentPermissionGranted)
            return

        pastPermissionGranted = currentPermissionGranted
        selectContentViewBy(currentPermissionGranted)
    }

    private fun permissionGranted(): Boolean {
        val permissionGrantedSets = NotificationManagerCompat.getEnabledListenerPackages(this)
        return permissionGrantedSets.contains(packageName)
    }

    private fun selectContentViewBy(granted: Boolean) {
        if (!granted) {
            setContentView(activityGrantBinding.root)
            grantButton.setOnClickListener {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)
            }
            return
        }

        setContentView(activityMainBinding.root)
        setSupportActionBar(toolbar)
        with (activityMainBinding) {
            viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, lifecycle)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.setText(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val preferences = getSharedPreferences("enabled", Context.MODE_PRIVATE)
        menuInflater.inflate(R.menu.appbar_actions, menu)
        with (menu.findItem(R.id.alarmSwitch).actionView.alarmSwitch) {
            isChecked = preferences.getBoolean("enabled", true)
            setOnClickListener {
                preferences.edit().putBoolean("enabled", isChecked).apply()
            }
            return true
        }
    }
}