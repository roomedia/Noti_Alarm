package com.roomedia.dawn_down_alarm.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.roomedia.dawn_down_alarm.databinding.ActivityMainBinding
import com.roomedia.dawn_down_alarm.presentation.view.fragment.GrantFragment
import com.roomedia.dawn_down_alarm.presentation.view.fragment.AppListFragment as AppListFragment1

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val prefs by lazy { getSharedPreferences(application.packageName, MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            selectFragmentBy(isPermissionGranted())
        }
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val prevIsGranted = prefs.getBoolean("is_granted", false)
        val currentIsGranted = isPermissionGranted()

        if (prevIsGranted == currentIsGranted) return
        selectFragmentBy(currentIsGranted)
    }

    override fun onPause() {
        super.onPause()
        prefs.edit().putBoolean("is_granted", isPermissionGranted()).apply()
    }

    private fun selectFragmentBy(isGranted: Boolean) {
        val fragment = when (isGranted) {
            true -> AppListFragment1()
            false -> GrantFragment()
        }
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            replace(binding.fragmentContainerView.id, fragment)
        }.commit()
    }

    private fun isPermissionGranted(): Boolean {
        return NotificationManagerCompat
            .getEnabledListenerPackages(this)
            .contains(packageName)
    }
}