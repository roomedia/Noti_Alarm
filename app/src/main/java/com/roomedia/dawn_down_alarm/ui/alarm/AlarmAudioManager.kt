package com.roomedia.dawn_down_alarm.ui.alarm

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import com.roomedia.dawn_down_alarm.BuildConfig
import com.roomedia.dawn_down_alarm.R

class AlarmAudioManager(private val context: Context) {
    // sound
    private val audioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val soundPool by lazy {
        SoundPool.Builder().setMaxStreams(1).build()
    }
    private var streamID: Int? = null
    private var volume = 0

    private fun isPlaying() = streamID != null

    fun turnOn() {
        if (isPlaying()) return

        // save current volume and maximize it
        volume = audioManager.getStreamVolume(STREAM_TYPE)
        val newVolume = if (BuildConfig.DEBUG) 1 else 15
        audioManager.setStreamVolume(STREAM_TYPE, newVolume, AUDIO_FLAG)

        // load and infinite play
        streamID = soundPool.load(context, R.raw.alarm, 1)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, _ ->
            soundPool.play(sampleId, 1f, 1f, 1, -1, 1f)
        }
    }

    fun turnOff() {
        audioManager.setStreamVolume(STREAM_TYPE, volume, AUDIO_FLAG)
        soundPool.stop(streamID ?: 1)
        streamID = null
    }

    companion object {
        const val STREAM_TYPE = AudioManager.STREAM_SYSTEM
        const val AUDIO_FLAG = AudioManager.FLAG_SHOW_UI
    }
}