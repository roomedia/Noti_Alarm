@file:Suppress("DEPRECATION")

package com.roomedia.dawn_down_alarm.presentation.view.activity

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.WindowManager.LayoutParams.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
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
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    private val audioManager by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private val soundPool by lazy {
        val streamType = AudioAttributes.Builder().setLegacyStreamType(STREAM_TYPE).build()
        SoundPool.Builder().setAudioAttributes(streamType).setMaxStreams(1).build()
    }
    private var streamID: Int? = null

    private val vibrator by lazy { getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

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
        if (streamID == null) return
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
        window.attributes.screenBrightness = prefs.getInt("brightness", 100) / 100f
        prefs.edit().putInt("user_volume", audioManager.getStreamVolume(STREAM_TYPE)).apply()
    }

    private fun startAlarm() {
        if (streamID != null) return
        startSound()
        startVibrate()
    }

    private fun startSound() {
        audioManager.setStreamVolume(STREAM_TYPE, prefs.getInt("volume", 15), STREAM_FLAG)
        streamID = soundPool.load(this, R.raw.alarm, 1)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, _ ->
            soundPool.play(sampleId, 1f, 1f, 1, -1, 1f)
        }
    }

    private fun startVibrate() {
        if (prefs.getBoolean("vibration", false)) {
            vibrator.vibrate(longArrayOf(100, 1000), 0)
        }
    }

    private fun stopAlarm() {
        stopSound()
        stopVibrate()
    }

    private fun stopSound() {
        audioManager.setStreamVolume(STREAM_TYPE, prefs.getInt("user_volume", 1), STREAM_FLAG)
        soundPool.stop(streamID ?: 1)
        streamID = null
    }

    private fun stopVibrate() {
        vibrator.cancel()
    }

    companion object {
        private const val STREAM_TYPE = AudioManager.STREAM_ALARM
        private const val STREAM_FLAG = AudioManager.FLAG_SHOW_UI
    }
}