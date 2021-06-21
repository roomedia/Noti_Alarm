@file:Suppress("DEPRECATION")

package com.roomedia.dawn_down_alarm.presentation.view.activity

import android.app.KeyguardManager
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.data.AppListViewModel
import com.roomedia.dawn_down_alarm.data.CommonViewModelFactory
import com.roomedia.dawn_down_alarm.databinding.ActivityAlarmBinding
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import com.roomedia.dawn_down_alarm.util.InterstitialManager
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmActivity : AppCompatActivity() {

    private var count = 4
    private val interstitialManager by lazy { InterstitialManager(this) }

    private val binding by lazy { ActivityAlarmBinding.inflate(layoutInflater) }
    private val prefs by lazy { getSharedPreferences(application.packageName, MODE_PRIVATE) }

    private val audioManager by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private val soundPool by lazy { SoundPool.Builder().setMaxStreams(1).build() }
    private var streamID: Int? = null
    private var volume = 0

    private val appListViewModel: AppListViewModel by viewModels {
        CommonViewModelFactory(AlarmApplication.instance.appDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interstitialManager.fetchAd()

        setContentView(binding.root)
        setAlarmContent()
        setInfiniteShow()
        setOptions()
        startAlarm()
    }

    override fun onBackPressed() = Unit

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }

    private fun setAlarmContent() {
        appListViewModel.get(intent.getStringExtra("packageName")!!)
            .subscribeOn(Schedulers.io())
            .subscribe({ (app) ->
                CoroutineScope(Dispatchers.Main).launch {
                    binding.iconImageView.setImageDrawable(app!!.icon)
                    binding.nameTextView.text = app.appName
                }
            }) {}

        binding.notificationTextView.text = intent.getStringExtra("notification")
        binding.awakeButton.text = getString(R.string.alarm_description, count)
        binding.awakeButton.setOnClickListener {
            when {
                count > 1 -> {
                    count -= 1
                    binding.awakeButton.text = getString(R.string.alarm_description, count)
                }
                count == 1 -> {
                    count = 0
                    stopAlarm()
                    binding.awakeButton.text = getString(R.string.watch_ad)
                    binding.closeButton.visibility = View.VISIBLE
                    binding.closeButton.setOnClickListener {
                        finish()
                    }
                }
                else -> {
                    interstitialManager.showAdIfAvailable()
                }
            }
        }
    }

    private fun setInfiniteShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                FLAG_DISMISS_KEYGUARD or
                        FLAG_SHOW_WHEN_LOCKED or
                        FLAG_TURN_SCREEN_ON
            )
        }
    }

    private fun setOptions() {
        window.attributes.screenBrightness = prefs.getFloat("brightness", 1.0f)
        volume = prefs.getInt("alarm_volume", 15)

        prefs.edit().putInt("user_volume", audioManager.getStreamVolume(STREAM_TYPE)).apply()
        // vibration = prefs.getInt("vibration", 0)
    }

    private fun startAlarm() {
        if (streamID != null) return
        audioManager.setStreamVolume(STREAM_TYPE, volume, STREAM_FLAG)
        streamID = soundPool.load(this, R.raw.alarm, 1)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, _ ->
            soundPool.play(sampleId, 1f, 1f, 1, -1, 1f)
        }
    }

    private fun stopAlarm() {
        volume = prefs.getInt("user_volume", 0)
        audioManager.setStreamVolume(STREAM_TYPE, volume, STREAM_FLAG)
        soundPool.stop(streamID ?: 1)
        streamID = null
    }

    companion object {
        private const val STREAM_TYPE = AudioManager.STREAM_ALARM
        private const val STREAM_FLAG = AudioManager.FLAG_SHOW_UI
    }
}