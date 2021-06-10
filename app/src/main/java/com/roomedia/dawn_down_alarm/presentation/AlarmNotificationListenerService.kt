package com.roomedia.dawn_down_alarm.presentation

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.roomedia.dawn_down_alarm.presentation.view.activity.MainActivity
import java.util.*

class AlarmNotificationListenerService : NotificationListenerService() {

    private val keywordDao = AlarmApplication.instance.keywordDao

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        val text = sbn.notification.extras.run {
            arrayOf(
                getString(Notification.EXTRA_TITLE, ""),
                getCharSequence(Notification.EXTRA_TEXT, ""),
                getCharSequence(Notification.EXTRA_SUB_TEXT, ""),
            ).joinToString("\n")
        }

        var isFirst = true
        keywordDao.get(sbn.packageName).observeForever { keywords ->
            if (!isFirst) return@observeForever
            isFirst = false

            if (!keywords.fold(false) { acc, (keyword) ->
                    acc || text.contains(keyword.toLowerCase(Locale.ROOT)) })
                        return@observeForever

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}