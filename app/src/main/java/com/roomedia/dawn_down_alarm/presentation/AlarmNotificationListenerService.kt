package com.roomedia.dawn_down_alarm.presentation

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.presentation.view.activity.AlarmActivity
import com.roomedia.dawn_down_alarm.util.getTimeValue
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AlarmNotificationListenerService : NotificationListenerService() {

    private val appDao = AlarmApplication.instance.appDao

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        appDao.getAppAndKeywords(sbn.packageName).subscribeOn(Schedulers.io()).subscribe({
            val (app, keywords) = it
            val notificationText = getNotificationFullText(sbn.notification.extras)
            val time = Calendar.getInstance().getTimeValue()
            when {
                app == null
                        || !app.isEnabled
                        || keywords.isEmpty()
                        || (app.startTime <= app.endTime && time !in app.startTime..app.endTime)
                        || (app.startTime > app.endTime && time !in app.startTime..1439 && time !in 0..app.endTime)
                        || notificationText.isEmpty()
                        || keywords.fold(true) { acc, (keyword) -> acc && !notificationText.contains(keyword.toLowerCase(Locale.ROOT)) }
                -> return@subscribe
                else -> startActivity(app, notificationText)
            }
        }) {}
    }

    private fun getNotificationFullText(bundle: Bundle): String {
        return listOf(
            bundle.getString(Notification.EXTRA_TITLE, ""),
            bundle.getCharSequence(Notification.EXTRA_TEXT, ""),
            bundle.getCharSequence(Notification.EXTRA_SUB_TEXT, "")
        ).joinToString(separator = "\n").toLowerCase(Locale.ROOT)
    }

    private fun startActivity(app: App, notificationText: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val intent = Intent(applicationContext, AlarmActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                .putExtra("packageName", app.packageName)
                .putExtra("notification", notificationText)
            startActivity(intent)
        }
    }
}