@file:Suppress("DEPRECATION")

package com.roomedia.dawn_down_alarm.presentation.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.databinding.ActivityAlarmBinding
import com.roomedia.dawn_down_alarm.util.InterstitialManager

class AlarmActivity : AppCompatActivity() {

    private var count = 4
    private val interstitialManager by lazy { InterstitialManager(this) }
    private val binding by lazy { ActivityAlarmBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interstitialManager.fetchAd()

        setContentView(binding.root)
        setAlarmContent()
    }

    private fun setAlarmContent() {
        binding.awakeButton.text = getString(R.string.alarm_description, count)
        binding.awakeButton.setOnClickListener {
            when {
                count > 1 -> {
                    count -= 1
                    binding.awakeButton.text = getString(R.string.alarm_description, count)
                }
                count == 1 -> {
                    count = 0
                    binding.awakeButton.text = getString(R.string.watch_ad)
                }
                else -> {
                    interstitialManager.showAdIfAvailable()
                    binding.closeButton.visibility = View.VISIBLE
                    binding.closeButton.setOnClickListener {
                        finish()
                    }
                }
            }
        }
    }
}