package com.roo_media_.noti_alarm

import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.SoundPool
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.roo_media_.noti_alarm.model.AppDatabase
import com.roo_media_.noti_alarm.model.KeywordDao

class NotificationAlarmListener: NotificationListenerService() {
    // db
    private lateinit var db: AppDatabase
    private lateinit var keywordDao: KeywordDao

    // sound
    private lateinit var audioManager: AudioManager
    private var soundPool = SoundPool.Builder().setMaxStreams(1).build()
    private var streamID: Int? = null
    private var volume = 0

    // enabled
    private lateinit var pref: SharedPreferences

    override fun onListenerConnected() {
        super.onListenerConnected()
        db = AppDatabase.getAppDataBase(applicationContext)!!
        keywordDao = db.keywordDao()
        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        pref = getSharedPreferences("enabled", Context.MODE_PRIVATE)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (!pref.getBoolean("enabled", false)) return
        if (streamID != null) return
        sbn ?: return

        val text = sbn.notification.extras.run {
            getString(Notification.EXTRA_TITLE) + " " +
            getCharSequence(Notification.EXTRA_TEXT) + " " +
            getCharSequence(Notification.EXTRA_SUB_TEXT)
        }

        keywordDao.get(sbn.packageName).observeForever { keywords ->
            // if not a registered app
            if (keywords.isEmpty())
                return@observeForever

            // if no keyword contained
            if (!keywords.fold(false) { acc, ele -> acc || text.contains(ele.keyword) })
                return@observeForever

            streamID = soundPool.load(applicationContext, R.raw.alarm, 1)
            soundPool.setOnLoadCompleteListener { soundPool, sampleId, _ ->
                volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, AudioManager.FLAG_SHOW_UI)
                soundPool.play(sampleId, 1f, 1f, 1, -1, 1f)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        streamID ?: return
        sbn ?: return

        keywordDao.get(sbn.packageName).observeForever { keywords ->
            // if not a registered app
            if (keywords.isEmpty())
                return@observeForever

            soundPool.stop(streamID!!)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI)
            streamID = null
        }
    }
}
