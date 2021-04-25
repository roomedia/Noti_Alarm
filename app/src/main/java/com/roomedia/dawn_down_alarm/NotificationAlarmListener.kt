package com.roomedia.dawn_down_alarm

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.Observer
import com.roomedia.dawn_down_alarm.model.AppDatabase
import com.roomedia.dawn_down_alarm.model.Keyword
import com.roomedia.dawn_down_alarm.ui.alarm.AlarmActivity
import java.util.*

class NotificationAlarmListener: NotificationListenerService() {
    private val keywordDao by lazy {
        AppDatabase.getAppDataBase(applicationContext)!!.keywordDao()
    }
    private val preferences by lazy {
        getSharedPreferences("enabled", Context.MODE_PRIVATE)
    }

    override fun onNotificationPosted(statusBarNotification: StatusBarNotification?) {
        if (!preferences.getBoolean("enabled", true)) return
        statusBarNotification ?: return

        val text = statusBarNotification.notification.extras.run {
            (getString(Notification.EXTRA_TITLE) ?: "") + "\n" +
            (getCharSequence(Notification.EXTRA_TEXT) ?: "") + "\n\n" +
            (getCharSequence(Notification.EXTRA_SUB_TEXT) ?: "")
        }

        var isFirst = true
        val alarmObserver = Observer<List<Keyword>> { keywords ->
            if (isFirst) {
                runAlarm(statusBarNotification.packageName, text.toLowerCase(Locale.getDefault()), keywords)
                isFirst = false
            }
        }
        keywordDao.get(statusBarNotification.packageName).observeForever(alarmObserver)
    }

    private fun runAlarm(packageName: String, text: String, keywords: List<Keyword>) {
        // if not a registered app
        if (keywords.isEmpty())
            return

        // if no keyword contained
        if (!keywords.fold(false) { acc, (keyword) ->
                acc || text.contains(keyword.toLowerCase(Locale.getDefault()))
            }) return

        val intent = Intent(applicationContext, AlarmActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .putExtra("packageName", packageName)
            .putExtra("name", keywords[0].name)
            .putExtra("notification", text)
        startActivity(intent)
    }
}
