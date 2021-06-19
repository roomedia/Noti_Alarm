package com.roomedia.dawn_down_alarm.util

import com.roomedia.dawn_down_alarm.R
import com.roomedia.dawn_down_alarm.entity.Keyword
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication

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
