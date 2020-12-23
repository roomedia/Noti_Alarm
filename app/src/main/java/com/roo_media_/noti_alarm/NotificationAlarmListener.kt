package com.roo_media_.noti_alarm

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.roo_media_.noti_alarm.model.AppDatabase
import com.roo_media_.noti_alarm.model.KeywordDao
import com.roo_media_.noti_alarm.ui.alarm.Alarm
import com.roo_media_.noti_alarm.ui.alarm.AlarmActivity

class NotificationAlarmListener: NotificationListenerService() {
    private val keywordDao: KeywordDao by lazy {
        AppDatabase.getAppDataBase(applicationContext)!!.keywordDao()
    }
    private val pref: SharedPreferences by lazy {
        getSharedPreferences("enabled", Context.MODE_PRIVATE)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (!pref.getBoolean("enabled", false)) return
        sbn ?: return

        val text = sbn.notification.extras.run {
            (getString(Notification.EXTRA_TITLE) ?: "") + "\n" +
            (getCharSequence(Notification.EXTRA_TEXT) ?: "") + "\n\n" +
            (getCharSequence(Notification.EXTRA_SUB_TEXT) ?: "")
        }

        keywordDao.get(sbn.packageName).observeForever { keywords ->
            // if not a registered app
            if (keywords.isEmpty())
                return@observeForever

            // if no keyword contained
            if (!keywords.fold(false) { acc, ele -> acc || text.contains(ele.keyword) })
                return@observeForever

            val intent = Intent(applicationContext, AlarmActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("packageName", sbn.packageName)
                .putExtra("name", keywords[0].name)
                .putExtra("notification", text)
            startActivity(intent)
        }
    }
}
