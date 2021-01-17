package com.roo_media_.noti_alarm

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.lifecycle.Observer
import com.roo_media_.noti_alarm.model.AppDatabase
import com.roo_media_.noti_alarm.model.Keyword
import com.roo_media_.noti_alarm.model.KeywordDao
import com.roo_media_.noti_alarm.ui.alarm.AlarmActivity
import java.util.*

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

        var isFirst = true
        val alarmObserver = Observer<List<Keyword>> { keywords ->
            if (isFirst) {
                alarm(sbn.packageName, text.toLowerCase(Locale.getDefault()), keywords)
                isFirst = false
            }
        }
        keywordDao.get(sbn.packageName).observeForever(alarmObserver)
    }

    private fun alarm(packageName: String, text: String, keywords: List<Keyword>) {

        // if not a registered app
        if (keywords.isEmpty())
            return

        // if no keyword contained
        if (!keywords.fold(false) { acc, ele ->
                acc || text.contains(ele.keyword.toLowerCase(Locale.getDefault())) })
            return

        val intent = Intent(applicationContext, AlarmActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .putExtra("packageName", packageName)
            .putExtra("name", keywords[0].name)
            .putExtra("notification", text)
        startActivity(intent)
    }
}
