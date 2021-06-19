package com.roomedia.dawn_down_alarm.util

import android.content.pm.ApplicationInfo
import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.entity.Keyword
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import java.util.*

fun Int.divmodTime(): Pair<Int, Int> {
    return Pair(this / 60, this % 60)
}

fun joinToString(startTime: Int, endTime: Int): String {
    val (startHour, startMinute) = startTime.divmodTime()
    val (endHour, endMinute) = endTime.divmodTime()
    return AlarmApplication.instance.getString(R.string.active_time, startHour, startMinute, endHour, endMinute)
}

fun List<Keyword>.joinToString(): String {
    return joinToString { it.keyword }
}

fun ApplicationInfo.isInstalledByUser(): Boolean {
    return flags and ApplicationInfo.FLAG_SYSTEM == 0
}

fun ApplicationInfo.toApp(timestamp: Long = Date().time): App {
    return AlarmApplication.instance.packageManager.let { packageManager ->
        App(packageName, loadLabel(packageManager).toString(), loadIcon(packageManager), timestamp)
    }
}