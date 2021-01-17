package com.roo_media_.noti_alarm.ui.alarm

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import com.roo_media_.noti_alarm.BuildConfig
import com.roo_media_.noti_alarm.R

class Alarm(private val context: Context) {
    // sound
    private val audioManager: AudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val soundPool: SoundPool by lazy {
        SoundPool.Builder().setMaxStreams(1).build()
    }
    private var streamID: Int? = null
    private var volume = 0

    private fun isPlaying() = streamID != null

    fun turnOn() {
        if (isPlaying()) return

        // save current volume and maximize it
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
//        val index = if (BuildConfig.DEBUG) 1 else 15
        val index = 15
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, AudioManager.FLAG_SHOW_UI)

        // load and infinite play
        streamID = soundPool.load(context, R.raw.alarm, 1)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, _ ->
            soundPool.play(sampleId, 1f, 1f, 1, -1, 1f)
        }
    }

    fun turnOff() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI)
        soundPool.stop(streamID ?: 1)
        streamID = null
    }
}